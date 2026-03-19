package com.switchboard.adapter.input.rest.dto;

import java.util.List;

public record CreateFlagRequest(
    String key,
    String name,
    String description,
    String flagType,
    String defaultVariant,
    List<VariantDto> variants
) {}
