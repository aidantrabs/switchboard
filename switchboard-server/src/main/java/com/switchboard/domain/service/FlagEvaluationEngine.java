package com.switchboard.domain.service;

import com.switchboard.domain.model.EvaluationContext;
import com.switchboard.domain.model.EvaluationResult;
import com.switchboard.domain.model.EvaluationResult.Reason;
import com.switchboard.domain.model.FeatureFlag;
import com.switchboard.domain.model.FlagEnvironmentConfig;
import com.switchboard.domain.model.Variant;

import java.util.Optional;

public final class FlagEvaluationEngine {

    private FlagEvaluationEngine() {}

    public static EvaluationResult evaluate(FeatureFlag flag, FlagEnvironmentConfig config,
                                            EvaluationContext context) {
        if (!config.isEnabled()) {
            return result(flag, flag.getDefaultVariant(), Reason.FLAG_DISABLED);
        }

        Optional<String> targetingMatch = TargetingRuleEvaluator.evaluate(
            config.getTargetingRules(), context);

        if (targetingMatch.isPresent()) {
            return result(flag, targetingMatch.get(), Reason.TARGETING_MATCH);
        }

        if (config.getRolloutPercentage() > 0 && context.userId() != null) {
            boolean inRollout = RolloutHasher.isInRollout(
                flag.getKey(), context.userId(), config.getRolloutPercentage());

            if (inRollout) {
                return result(flag, firstEnabledVariant(flag), Reason.ROLLOUT);
            }
        }

        return result(flag, flag.getDefaultVariant(), Reason.DEFAULT);
    }

    private static EvaluationResult result(FeatureFlag flag, String variantKey, Reason reason) {
        String value = flag.getVariants().stream()
            .filter(v -> v.key().equals(variantKey))
            .map(Variant::value)
            .findFirst()
            .orElse(null);

        return new EvaluationResult(flag.getKey(), variantKey, value, reason);
    }

    private static String firstEnabledVariant(FeatureFlag flag) {
        return flag.getVariants().stream()
            .map(Variant::key)
            .filter(key -> !key.equals(flag.getDefaultVariant()))
            .findFirst()
            .orElse(flag.getDefaultVariant());
    }
}
