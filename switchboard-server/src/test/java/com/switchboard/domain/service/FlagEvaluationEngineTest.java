package com.switchboard.domain.service;

import com.switchboard.domain.model.*;
import com.switchboard.domain.model.EvaluationResult.Reason;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlagEvaluationEngineTest {

    private static final List<Variant> VARIANTS = List.of(
        new Variant("on", "true"),
        new Variant("off", "false")
    );

    @Test
    void disabledFlagReturnsDefault() {
        var flag = FeatureFlag.create(UUID.randomUUID(), "test-flag", "Test",
            "desc", FlagType.RELEASE, "off", VARIANTS);
        var config = FlagEnvironmentConfig.create(flag.getId(), UUID.randomUUID());
        var context = new EvaluationContext("user-1", Map.of());

        EvaluationResult result = FlagEvaluationEngine.evaluate(flag, config, context);

        assertEquals("off", result.variantKey());
        assertEquals("false", result.value());
        assertEquals(Reason.FLAG_DISABLED, result.reason());
    }

    @Test
    void targetingMatchReturnMatchedVariant() {
        var flag = FeatureFlag.create(UUID.randomUUID(), "test-flag", "Test",
            "desc", FlagType.RELEASE, "off", VARIANTS);
        var config = FlagEnvironmentConfig.create(flag.getId(), UUID.randomUUID());
        config.toggle();
        config.updateTargetingRules(List.of(
            TargetingRule.create(1,
                List.of(new Condition("user.country", Operator.EQUALS, "us")),
                "on")
        ));
        var context = new EvaluationContext("user-1", Map.of("user.country", "us"));

        EvaluationResult result = FlagEvaluationEngine.evaluate(flag, config, context);

        assertEquals("on", result.variantKey());
        assertEquals(Reason.TARGETING_MATCH, result.reason());
    }

    @Test
    void noTargetingMatchFallsToRollout() {
        var flag = FeatureFlag.create(UUID.randomUUID(), "test-flag", "Test",
            "desc", FlagType.RELEASE, "off", VARIANTS);
        var config = FlagEnvironmentConfig.create(flag.getId(), UUID.randomUUID());
        config.toggle();
        config.updateRolloutPercentage(100);
        var context = new EvaluationContext("user-1", Map.of());

        EvaluationResult result = FlagEvaluationEngine.evaluate(flag, config, context);

        assertEquals("on", result.variantKey());
        assertEquals(Reason.ROLLOUT, result.reason());
    }

    @Test
    void zeroRolloutReturnsDefault() {
        var flag = FeatureFlag.create(UUID.randomUUID(), "test-flag", "Test",
            "desc", FlagType.RELEASE, "off", VARIANTS);
        var config = FlagEnvironmentConfig.create(flag.getId(), UUID.randomUUID());
        config.toggle();
        var context = new EvaluationContext("user-1", Map.of());

        EvaluationResult result = FlagEvaluationEngine.evaluate(flag, config, context);

        assertEquals("off", result.variantKey());
        assertEquals(Reason.DEFAULT, result.reason());
    }

    @Test
    void nullUserIdSkipsRollout() {
        var flag = FeatureFlag.create(UUID.randomUUID(), "test-flag", "Test",
            "desc", FlagType.RELEASE, "off", VARIANTS);
        var config = FlagEnvironmentConfig.create(flag.getId(), UUID.randomUUID());
        config.toggle();
        config.updateRolloutPercentage(100);
        var context = new EvaluationContext(null, Map.of());

        EvaluationResult result = FlagEvaluationEngine.evaluate(flag, config, context);

        assertEquals("off", result.variantKey());
        assertEquals(Reason.DEFAULT, result.reason());
    }
}
