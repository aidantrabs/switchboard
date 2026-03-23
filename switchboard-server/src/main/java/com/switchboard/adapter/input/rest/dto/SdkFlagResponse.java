package com.switchboard.adapter.input.rest.dto;

import com.switchboard.domain.model.FeatureFlag;
import com.switchboard.domain.model.FlagEnvironmentConfig;

import java.util.List;

public record SdkFlagResponse(
    String key,
    boolean enabled,
    String defaultVariant,
    List<VariantDto> variants,
    int rolloutPercentage,
    List<TargetingRuleDto> targetingRules
) {

    public static SdkFlagResponse from(FeatureFlag flag, FlagEnvironmentConfig config) {
        List<VariantDto> variants = flag.getVariants().stream()
            .map(v -> new VariantDto(v.key(), v.value()))
            .toList();

        List<TargetingRuleDto> rules = config.getTargetingRules().stream()
            .map(r -> new TargetingRuleDto(
                r.getPriority(),
                r.getConditions().stream()
                    .map(c -> new ConditionDto(c.attribute(), c.operator().name(), c.value()))
                    .toList(),
                r.getServedVariantKey()))
            .toList();

        return new SdkFlagResponse(
            flag.getKey(), config.isEnabled(), flag.getDefaultVariant(),
            variants, config.getRolloutPercentage(), rules
        );
    }
}
