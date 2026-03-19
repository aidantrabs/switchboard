package com.switchboard.adapter.input.rest.dto;

import java.util.List;

public record TargetingRuleDto(
    int priority,
    List<ConditionDto> conditions,
    String servedVariantKey
) {}
