package com.switchboard.adapter.input.rest.dto;

import com.switchboard.domain.model.FeatureFlag;

import java.time.Instant;
import java.util.List;

public record FlagResponse(
    String id,
    String key,
    String name,
    String description,
    String flagType,
    String defaultVariant,
    List<VariantDto> variants,
    Instant createdAt,
    Instant updatedAt
) {

    public static FlagResponse from(FeatureFlag flag) {
        List<VariantDto> variants = flag.getVariants().stream()
            .map(v -> new VariantDto(v.key(), v.value()))
            .toList();

        return new FlagResponse(
            flag.getId().toString(), flag.getKey(), flag.getName(),
            flag.getDescription(), flag.getFlagType().name(),
            flag.getDefaultVariant(), variants,
            flag.getCreatedAt(), flag.getUpdatedAt()
        );
    }
}
