package com.switchboard.domain.model;

import java.time.Instant;
import java.util.UUID;

public class AuditLog {

    private final UUID id;
    private final UUID projectId;
    private final String flagKey;
    private final String environmentKey;
    private final String action;
    private final String changedBy;
    private final String beforeState;
    private final String afterState;
    private final Instant timestamp;

    public AuditLog(UUID id, UUID projectId, String flagKey, String environmentKey,
                    String action, String changedBy, String beforeState,
                    String afterState, Instant timestamp) {
        this.id = id;
        this.projectId = projectId;
        this.flagKey = flagKey;
        this.environmentKey = environmentKey;
        this.action = action;
        this.changedBy = changedBy;
        this.beforeState = beforeState;
        this.afterState = afterState;
        this.timestamp = timestamp;
    }

    public static AuditLog create(UUID projectId, String flagKey, String environmentKey,
                                  String action, String changedBy,
                                  String beforeState, String afterState) {
        return new AuditLog(UUID.randomUUID(), projectId, flagKey, environmentKey,
            action, changedBy, beforeState, afterState, Instant.now());
    }

    public UUID getId() {
        return id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public String getFlagKey() {
        return flagKey;
    }

    public String getEnvironmentKey() {
        return environmentKey;
    }

    public String getAction() {
        return action;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public String getBeforeState() {
        return beforeState;
    }

    public String getAfterState() {
        return afterState;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
