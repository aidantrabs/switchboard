package com.switchboard.adapter.output.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "flag_environment_configs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"flag_id", "environment_id"})
})
public class FlagEnvironmentConfigJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "flag_id", nullable = false)
    private UUID flagId;

    @Column(name = "environment_id", nullable = false)
    private UUID environmentId;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "rollout_percentage", nullable = false)
    private int rolloutPercentage;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected FlagEnvironmentConfigJpaEntity() {}

    public FlagEnvironmentConfigJpaEntity(UUID id, UUID flagId, UUID environmentId,
                                          boolean enabled, int rolloutPercentage,
                                          Instant updatedAt) {
        this.id = id;
        this.flagId = flagId;
        this.environmentId = environmentId;
        this.enabled = enabled;
        this.rolloutPercentage = rolloutPercentage;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public UUID getFlagId() { return flagId; }
    public UUID getEnvironmentId() { return environmentId; }
    public boolean isEnabled() { return enabled; }
    public int getRolloutPercentage() { return rolloutPercentage; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setRolloutPercentage(int rolloutPercentage) { this.rolloutPercentage = rolloutPercentage; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
