package com.switchboard.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class FlagEnvironmentConfig {

    private final UUID id;
    private final UUID flagId;
    private final UUID environmentId;
    private boolean enabled;
    private int rolloutPercentage;
    private List<TargetingRule> targetingRules;
    private Instant updatedAt;

    public FlagEnvironmentConfig(UUID id, UUID flagId, UUID environmentId,
                                 boolean enabled, int rolloutPercentage,
                                 List<TargetingRule> targetingRules, Instant updatedAt) {
        this.id = id;
        this.flagId = flagId;
        this.environmentId = environmentId;
        this.enabled = enabled;
        this.rolloutPercentage = rolloutPercentage;
        this.targetingRules = List.copyOf(targetingRules);
        this.updatedAt = updatedAt;
    }

    public static FlagEnvironmentConfig create(UUID flagId, UUID environmentId) {
        return new FlagEnvironmentConfig(
            UUID.randomUUID(), flagId, environmentId,
            false, 0, List.of(), Instant.now()
        );
    }

    public void toggle() {
        this.enabled = !this.enabled;
        this.updatedAt = Instant.now();
    }

    public void updateRolloutPercentage(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("rollout percentage must be between 0 and 100");
        }
        this.rolloutPercentage = percentage;
        this.updatedAt = Instant.now();
    }

    public void updateTargetingRules(List<TargetingRule> rules) {
        this.targetingRules = List.copyOf(rules);
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getFlagId() {
        return flagId;
    }

    public UUID getEnvironmentId() {
        return environmentId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getRolloutPercentage() {
        return rolloutPercentage;
    }

    public List<TargetingRule> getTargetingRules() {
        return targetingRules;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
