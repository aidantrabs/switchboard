package com.switchboard.domain.model;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class FeatureFlag {

    private final UUID id;
    private final UUID projectId;
    private final String key;
    private String name;
    private String description;
    private final FlagType flagType;
    private String defaultVariant;
    private List<Variant> variants;
    private final Duration staleAfter;
    private final Instant createdAt;
    private Instant updatedAt;

    public FeatureFlag(UUID id, UUID projectId, String key, String name,
                       String description, FlagType flagType, String defaultVariant,
                       List<Variant> variants, Duration staleAfter,
                       Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.projectId = projectId;
        this.key = key;
        this.name = name;
        this.description = description;
        this.flagType = flagType;
        this.defaultVariant = defaultVariant;
        this.variants = List.copyOf(variants);
        this.staleAfter = staleAfter;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static FeatureFlag create(UUID projectId, String key, String name,
                                     String description, FlagType flagType,
                                     String defaultVariant, List<Variant> variants) {
        Instant now = Instant.now();
        return new FeatureFlag(
            UUID.randomUUID(), projectId, key, name, description,
            flagType, defaultVariant, variants, null, now, now
        );
    }

    public void update(String name, String description, String defaultVariant,
                       List<Variant> variants) {
        this.name = name;
        this.description = description;
        this.defaultVariant = defaultVariant;
        this.variants = List.copyOf(variants);
        this.updatedAt = Instant.now();
    }

    public boolean isStale() {
        if (staleAfter == null) {
            return false;
        }
        return Instant.now().isAfter(updatedAt.plus(staleAfter));
    }

    public UUID getId() {
        return id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public FlagType getFlagType() {
        return flagType;
    }

    public String getDefaultVariant() {
        return defaultVariant;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public Duration getStaleAfter() {
        return staleAfter;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
