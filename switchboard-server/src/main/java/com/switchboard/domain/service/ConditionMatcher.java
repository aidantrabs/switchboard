package com.switchboard.domain.service;

import com.switchboard.domain.model.Condition;
import com.switchboard.domain.model.EvaluationContext;

import java.util.Arrays;
import java.util.List;

public final class ConditionMatcher {

    private ConditionMatcher() {}

    public static boolean matches(Condition condition, EvaluationContext context) {
        String actual = context.getAttribute(condition.attribute());
        if (actual == null) {
            return false;
        }

        String expected = condition.value();

        return switch (condition.operator()) {
            case EQUALS -> actual.equals(expected);
            case NOT_EQUALS -> !actual.equals(expected);
            case CONTAINS -> actual.contains(expected);
            case STARTS_WITH -> actual.startsWith(expected);
            case IN_LIST -> parseList(expected).contains(actual);
            case NOT_IN_LIST -> !parseList(expected).contains(actual);
            case GREATER_THAN -> compareNumeric(actual, expected) > 0;
            case LESS_THAN -> compareNumeric(actual, expected) < 0;
            case SEMVER_GT -> compareSemver(actual, expected) > 0;
            case SEMVER_LT -> compareSemver(actual, expected) < 0;
        };
    }

    private static List<String> parseList(String value) {
        String trimmed = value.strip();
        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }

        return Arrays.stream(trimmed.split(","))
            .map(String::strip)
            .map(s -> s.startsWith("\"") && s.endsWith("\"") ? s.substring(1, s.length() - 1) : s)
            .toList();
    }

    private static int compareNumeric(String actual, String expected) {
        try {
            return Double.compare(Double.parseDouble(actual), Double.parseDouble(expected));
        } catch (NumberFormatException e) {
            return actual.compareTo(expected);
        }
    }

    private static int compareSemver(String actual, String expected) {
        int[] a = parseSemverParts(actual);
        int[] b = parseSemverParts(expected);

        for (int i = 0; i < 3; i++) {
            if (a[i] != b[i]) {
                return Integer.compare(a[i], b[i]);
            }
        }

        return 0;
    }

    private static int[] parseSemverParts(String version) {
        String[] parts = version.split("\\.");
        int[] result = new int[3];

        for (int i = 0; i < Math.min(parts.length, 3); i++) {
            try {
                result[i] = Integer.parseInt(parts[i]);
            } catch (NumberFormatException e) {
                result[i] = 0;
            }
        }

        return result;
    }
}
