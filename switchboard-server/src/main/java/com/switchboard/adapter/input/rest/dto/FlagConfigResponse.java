package com.switchboard.adapter.input.rest.dto;

import com.switchboard.domain.model.FlagEnvironmentConfig;

import java.util.List;

public record FlagConfigResponse(
    String flagId,
    boolean enabled,
    int rolloutPercentage,
    List<TargetingRuleDto> targetingRules
) {

    public static FlagConfigResponse from(FlagEnvironmentConfig config) {
        List<TargetingRuleDto> rules = config.getTargetingRules().stream()
            .map(r -> new TargetingRuleDto(
                r.getPriority(),
                r.getConditions().stream()
                    .map(c -> new ConditionDto(c.attribute(), c.operator().name(), c.value()))
                    .toList(),
                r.getServedVariantKey()))
            .toList();

        return new FlagConfigResponse(
            config.getFlagId().toString(), config.isEnabled(),
            config.getRolloutPercentage(), rules
        );
    }
}
