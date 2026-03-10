package com.switchboard.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Project {

    private final UUID id;
    private final UUID organizationId;
    private final String name;
    private final String key;
    private final Instant createdAt;

    public Project(UUID id, UUID organizationId, String name, String key, Instant createdAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.key = key;
        this.createdAt = createdAt;
    }

    public static Project create(UUID organizationId, String name, String key) {
        return new Project(UUID.randomUUID(), organizationId, name, key, Instant.now());
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
