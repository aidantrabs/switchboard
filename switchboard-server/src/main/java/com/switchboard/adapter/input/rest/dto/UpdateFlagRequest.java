package com.switchboard.adapter.input.rest.dto;

import java.util.List;

public record UpdateFlagRequest(
    String name,
    String description,
    String defaultVariant,
    List<VariantDto> variants
) {}
