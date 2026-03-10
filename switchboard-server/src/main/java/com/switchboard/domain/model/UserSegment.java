package com.switchboard.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class UserSegment {

    private final UUID id;
    private final UUID projectId;
    private String name;
    private String description;
    private List<Condition> conditions;
    private final Instant createdAt;

    public UserSegment(UUID id, UUID projectId, String name, String description,
                       List<Condition> conditions, Instant createdAt) {
        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.conditions = List.copyOf(conditions);
        this.createdAt = createdAt;
    }

    public static UserSegment create(UUID projectId, String name, String description,
                                     List<Condition> conditions) {
        return new UserSegment(UUID.randomUUID(), projectId, name, description,
            conditions, Instant.now());
    }

    public void update(String name, String description, List<Condition> conditions) {
        this.name = name;
        this.description = description;
        this.conditions = List.copyOf(conditions);
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

    public String getDescription() {
        return description;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
