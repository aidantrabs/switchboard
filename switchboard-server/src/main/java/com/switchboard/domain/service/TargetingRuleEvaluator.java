package com.switchboard.domain.service;

import com.switchboard.domain.model.EvaluationContext;
import com.switchboard.domain.model.TargetingRule;

import java.util.List;
import java.util.Optional;

public final class TargetingRuleEvaluator {

    private TargetingRuleEvaluator() {}

    public static Optional<String> evaluate(List<TargetingRule> rules, EvaluationContext context) {
        return rules.stream()
            .sorted((a, b) -> Integer.compare(a.getPriority(), b.getPriority()))
            .filter(rule -> matchesAll(rule, context))
            .map(TargetingRule::getServedVariantKey)
            .findFirst();
    }

    private static boolean matchesAll(TargetingRule rule, EvaluationContext context) {
        return rule.getConditions().stream()
            .allMatch(condition -> ConditionMatcher.matches(condition, context));
    }
}
