package com.switchboard.sdk;

import com.switchboard.sdk.model.Condition;
import com.switchboard.sdk.model.FlagConfig;
import com.switchboard.sdk.model.TargetingRule;
import com.switchboard.sdk.model.Variant;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FlagEvaluator {

    public String evaluate(FlagConfig flag, EvaluationContext context) {
        if (!flag.isEnabled()) {
            return flag.getDefaultVariant();
        }

        Optional<String> targetingMatch = evaluateTargetingRules(flag.getTargetingRules(), context);
        if (targetingMatch.isPresent()) {
            return targetingMatch.get();
        }

        if (flag.getRolloutPercentage() > 0 && context.getUserId() != null) {
            if (isInRollout(flag.getKey(), context.getUserId(), flag.getRolloutPercentage())) {
                return firstNonDefaultVariant(flag);
            }
        }

        return flag.getDefaultVariant();
    }

    private Optional<String> evaluateTargetingRules(List<TargetingRule> rules, EvaluationContext context) {
        if (rules == null) return Optional.empty();

        return rules.stream()
            .sorted((a, b) -> Integer.compare(a.getPriority(), b.getPriority()))
            .filter(rule -> matchesAll(rule.getConditions(), context))
            .map(TargetingRule::getServedVariantKey)
            .findFirst();
    }

    private boolean matchesAll(List<Condition> conditions, EvaluationContext context) {
        return conditions.stream().allMatch(c -> matchCondition(c, context));
    }

    private boolean matchCondition(Condition condition, EvaluationContext context) {
        String actual = context.getAttribute(condition.getAttribute());
        if (actual == null) return false;

        String expected = condition.getValue();

        return switch (condition.getOperator()) {
            case "EQUALS" -> actual.equals(expected);
            case "NOT_EQUALS" -> !actual.equals(expected);
            case "CONTAINS" -> actual.contains(expected);
            case "STARTS_WITH" -> actual.startsWith(expected);
            case "IN_LIST" -> parseList(expected).contains(actual);
            case "NOT_IN_LIST" -> !parseList(expected).contains(actual);
            case "GREATER_THAN" -> compareNumeric(actual, expected) > 0;
            case "LESS_THAN" -> compareNumeric(actual, expected) < 0;
            default -> false;
        };
    }

    private List<String> parseList(String value) {
        String trimmed = value.strip();
        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        return Arrays.stream(trimmed.split(","))
            .map(String::strip)
            .map(s -> s.startsWith("\"") && s.endsWith("\"") ? s.substring(1, s.length() - 1) : s)
            .toList();
    }

    private int compareNumeric(String actual, String expected) {
        try {
            return Double.compare(Double.parseDouble(actual), Double.parseDouble(expected));
        } catch (NumberFormatException e) {
            return actual.compareTo(expected);
        }
    }

    private String firstNonDefaultVariant(FlagConfig flag) {
        if (flag.getVariants() == null) return flag.getDefaultVariant();
        return flag.getVariants().stream()
            .map(Variant::getKey)
            .filter(key -> !key.equals(flag.getDefaultVariant()))
            .findFirst()
            .orElse(flag.getDefaultVariant());
    }

    static boolean isInRollout(String flagKey, String userId, int percentage) {
        if (percentage <= 0) return false;
        if (percentage >= 100) return true;
        String input = flagKey + ":" + userId;
        int hash = murmurhash3(input.getBytes(StandardCharsets.UTF_8));
        return Math.abs(hash % 100) < percentage;
    }

    private static int murmurhash3(byte[] data) {
        int h = 0;
        int length = data.length;
        int roundedEnd = length & ~3;

        for (int i = 0; i < roundedEnd; i += 4) {
            int k = (data[i] & 0xff) | ((data[i + 1] & 0xff) << 8)
                | ((data[i + 2] & 0xff) << 16) | ((data[i + 3] & 0xff) << 24);
            k *= 0xcc9e2d51;
            k = Integer.rotateLeft(k, 15);
            k *= 0x1b873593;
            h ^= k;
            h = Integer.rotateLeft(h, 13);
            h = h * 5 + 0xe6546b64;
        }

        int k = 0;
        switch (length & 3) {
            case 3: k ^= (data[roundedEnd + 2] & 0xff) << 16;
            case 2: k ^= (data[roundedEnd + 1] & 0xff) << 8;
            case 1:
                k ^= (data[roundedEnd] & 0xff);
                k *= 0xcc9e2d51;
                k = Integer.rotateLeft(k, 15);
                k *= 0x1b873593;
                h ^= k;
        }

        h ^= length;
        h ^= h >>> 16;
        h *= 0x85ebca6b;
        h ^= h >>> 13;
        h *= 0xc2b2ae35;
        h ^= h >>> 16;

        return h;
    }
}
