package com.switchboard.adapter.output.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "projects", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"organization_id", "key"})
})
public class ProjectJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String key;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ProjectJpaEntity() {}

    public ProjectJpaEntity(UUID id, UUID organizationId, String name, String key, Instant createdAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.key = key;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getOrganizationId() { return organizationId; }
    public String getName() { return name; }
    public String getKey() { return key; }
    public Instant getCreatedAt() { return createdAt; }
}
