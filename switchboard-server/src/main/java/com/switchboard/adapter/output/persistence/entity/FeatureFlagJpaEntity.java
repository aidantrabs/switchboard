package com.switchboard.adapter.output.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "feature_flags", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"project_id", "key"})
})
public class FeatureFlagJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(nullable = false)
    private String key;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "flag_type", nullable = false)
    private String flagType;

    @Column(name = "default_variant", nullable = false)
    private String defaultVariant;

    @Column(name = "stale_after")
    private Long staleAfter;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "flagId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VariantJpaEntity> variants = new ArrayList<>();

    protected FeatureFlagJpaEntity() {}

    public FeatureFlagJpaEntity(UUID id, UUID projectId, String key, String name,
                                String description, String flagType, String defaultVariant,
                                Long staleAfter, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.projectId = projectId;
        this.key = key;
        this.name = name;
        this.description = description;
        this.flagType = flagType;
        this.defaultVariant = defaultVariant;
        this.staleAfter = staleAfter;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public UUID getProjectId() { return projectId; }
    public String getKey() { return key; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getFlagType() { return flagType; }
    public String getDefaultVariant() { return defaultVariant; }
    public Long getStaleAfter() { return staleAfter; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<VariantJpaEntity> getVariants() { return variants; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setDefaultVariant(String defaultVariant) { this.defaultVariant = defaultVariant; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setVariants(List<VariantJpaEntity> variants) { this.variants = variants; }
}
