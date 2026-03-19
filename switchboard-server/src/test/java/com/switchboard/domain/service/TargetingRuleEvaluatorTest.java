package com.switchboard.domain.service;

import com.switchboard.domain.model.Condition;
import com.switchboard.domain.model.EvaluationContext;
import com.switchboard.domain.model.Operator;
import com.switchboard.domain.model.TargetingRule;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TargetingRuleEvaluatorTest {

    @Test
    void returnsEmptyWhenNoRules() {
        var context = new EvaluationContext("user-1", Map.of());

        Optional<String> result = TargetingRuleEvaluator.evaluate(List.of(), context);

        assertTrue(result.isEmpty());
    }

    @Test
    void returnsEmptyWhenNoRuleMatches() {
        var rule = TargetingRule.create(1,
            List.of(new Condition("user.country", Operator.EQUALS, "us")),
            "on");
        var context = new EvaluationContext("user-1", Map.of("user.country", "ca"));

        Optional<String> result = TargetingRuleEvaluator.evaluate(List.of(rule), context);

        assertTrue(result.isEmpty());
    }

    @Test
    void returnsVariantWhenAllConditionsMatch() {
        var rule = TargetingRule.create(1, List.of(
            new Condition("user.country", Operator.EQUALS, "us"),
            new Condition("user.plan", Operator.IN_LIST, "[\"premium\", \"enterprise\"]")
        ), "enabled");
        var context = new EvaluationContext("user-1",
            Map.of("user.country", "us", "user.plan", "premium"));

        Optional<String> result = TargetingRuleEvaluator.evaluate(List.of(rule), context);

        assertEquals(Optional.of("enabled"), result);
    }

    @Test
    void rejectsWhenOneConditionFails() {
        var rule = TargetingRule.create(1, List.of(
            new Condition("user.country", Operator.EQUALS, "us"),
            new Condition("user.plan", Operator.EQUALS, "premium")
        ), "enabled");
        var context = new EvaluationContext("user-1",
            Map.of("user.country", "us", "user.plan", "free"));

        Optional<String> result = TargetingRuleEvaluator.evaluate(List.of(rule), context);

        assertTrue(result.isEmpty());
    }

    @Test
    void firstMatchingRuleWinsByPriority() {
        var rule1 = TargetingRule.create(1,
            List.of(new Condition("user.country", Operator.EQUALS, "us")),
            "variant-a");
        var rule2 = TargetingRule.create(2,
            List.of(new Condition("user.country", Operator.EQUALS, "us")),
            "variant-b");
        var context = new EvaluationContext("user-1", Map.of("user.country", "us"));

        Optional<String> result = TargetingRuleEvaluator.evaluate(List.of(rule2, rule1), context);

        assertEquals(Optional.of("variant-a"), result);
    }

    @Test
    void skipsNonMatchingRuleAndMatchesLaterOne() {
        var rule1 = TargetingRule.create(1,
            List.of(new Condition("user.country", Operator.EQUALS, "ca")),
            "variant-a");
        var rule2 = TargetingRule.create(2,
            List.of(new Condition("user.country", Operator.EQUALS, "us")),
            "variant-b");
        var context = new EvaluationContext("user-1", Map.of("user.country", "us"));

        Optional<String> result = TargetingRuleEvaluator.evaluate(List.of(rule1, rule2), context);

        assertEquals(Optional.of("variant-b"), result);
    }
}
