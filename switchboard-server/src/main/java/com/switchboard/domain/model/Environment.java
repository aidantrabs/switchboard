package com.switchboard.domain.model;

import java.util.UUID;

public class Environment {

    private final UUID id;
    private final UUID projectId;
    private final String name;
    private final String key;
    private final int sortOrder;

    public Environment(UUID id, UUID projectId, String name, String key, int sortOrder) {
        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.key = key;
        this.sortOrder = sortOrder;
    }

    public static Environment create(UUID projectId, String name, String key, int sortOrder) {
        return new Environment(UUID.randomUUID(), projectId, name, key, sortOrder);
    }

    public UUID getId() {
        return id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
