package com.switchboard.adapter.output.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "targeting_rules")
public class TargetingRuleJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "flag_env_config_id", nullable = false)
    private UUID flagEnvConfigId;

    @Column(nullable = false)
    private int priority;

    @Column(name = "served_variant_key", nullable = false)
    private String servedVariantKey;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "targetingRuleId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConditionJpaEntity> conditions = new ArrayList<>();

    protected TargetingRuleJpaEntity() {}

    public TargetingRuleJpaEntity(UUID id, UUID flagEnvConfigId, int priority,
                                  String servedVariantKey, Instant createdAt) {
        this.id = id;
        this.flagEnvConfigId = flagEnvConfigId;
        this.priority = priority;
        this.servedVariantKey = servedVariantKey;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getFlagEnvConfigId() { return flagEnvConfigId; }
    public int getPriority() { return priority; }
    public String getServedVariantKey() { return servedVariantKey; }
    public Instant getCreatedAt() { return createdAt; }
    public List<ConditionJpaEntity> getConditions() { return conditions; }

    public void setConditions(List<ConditionJpaEntity> conditions) { this.conditions = conditions; }
}
