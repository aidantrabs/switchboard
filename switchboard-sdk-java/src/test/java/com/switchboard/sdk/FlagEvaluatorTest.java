package com.switchboard.sdk;

import com.switchboard.sdk.model.Condition;
import com.switchboard.sdk.model.FlagConfig;
import com.switchboard.sdk.model.TargetingRule;
import com.switchboard.sdk.model.Variant;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlagEvaluatorTest {

    private final FlagEvaluator evaluator = new FlagEvaluator();

    private static final List<Variant> VARIANTS = List.of(
        new Variant("on", "true"),
        new Variant("off", "false")
    );

    @Test
    void disabledFlagReturnsDefault() {
        var flag = new FlagConfig("flag-1", false, "off", VARIANTS, 0, List.of());
        var ctx = EvaluationContext.builder().userId("user-1").build();

        assertEquals("off", evaluator.evaluate(flag, ctx));
    }

    @Test
    void enabledFlagWithNoRulesAndNoRolloutReturnsDefault() {
        var flag = new FlagConfig("flag-1", true, "off", VARIANTS, 0, List.of());
        var ctx = EvaluationContext.builder().userId("user-1").build();

        assertEquals("off", evaluator.evaluate(flag, ctx));
    }

    @Test
    void targetingRuleMatchReturnsServedVariant() {
        var rule = new TargetingRule(1,
            List.of(new Condition("user.country", "EQUALS", "us")),
            "on");
        var flag = new FlagConfig("flag-1", true, "off", VARIANTS, 0, List.of(rule));
        var ctx = EvaluationContext.builder()
            .userId("user-1")
            .attribute("user.country", "us")
            .build();

        assertEquals("on", evaluator.evaluate(flag, ctx));
    }

    @Test
    void targetingRuleMissGoesToRollout() {
        var rule = new TargetingRule(1,
            List.of(new Condition("user.country", "EQUALS", "ca")),
            "on");
        var flag = new FlagConfig("flag-1", true, "off", VARIANTS, 100, List.of(rule));
        var ctx = EvaluationContext.builder()
            .userId("user-1")
            .attribute("user.country", "us")
            .build();

        assertEquals("on", evaluator.evaluate(flag, ctx));
    }

    @Test
    void fullRolloutReturnsNonDefaultVariant() {
        var flag = new FlagConfig("flag-1", true, "off", VARIANTS, 100, List.of());
        var ctx = EvaluationContext.builder().userId("user-1").build();

        assertEquals("on", evaluator.evaluate(flag, ctx));
    }

    @Test
    void nullUserIdSkipsRollout() {
        var flag = new FlagConfig("flag-1", true, "off", VARIANTS, 100, List.of());
        var ctx = EvaluationContext.builder().build();

        assertEquals("off", evaluator.evaluate(flag, ctx));
    }

    @Test
    void rolloutIsDeterministic() {
        var flag = new FlagConfig("flag-1", true, "off", VARIANTS, 50, List.of());
        var ctx = EvaluationContext.builder().userId("user-1").build();

        String first = evaluator.evaluate(flag, ctx);
        String second = evaluator.evaluate(flag, ctx);

        assertEquals(first, second);
    }
}
