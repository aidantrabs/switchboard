package com.switchboard.adapter.output.persistence.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "environments", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"project_id", "key"})
})
public class EnvironmentJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String key;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    protected EnvironmentJpaEntity() {}

    public EnvironmentJpaEntity(UUID id, UUID projectId, String name, String key, int sortOrder) {
        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.key = key;
        this.sortOrder = sortOrder;
    }

    public UUID getId() { return id; }
    public UUID getProjectId() { return projectId; }
    public String getName() { return name; }
    public String getKey() { return key; }
    public int getSortOrder() { return sortOrder; }
}
