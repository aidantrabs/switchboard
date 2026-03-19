package com.switchboard.adapter.output.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class AuditLogJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(name = "flag_key")
    private String flagKey;

    @Column(name = "environment_key")
    private String environmentKey;

    @Column(nullable = false)
    private String action;

    @Column(name = "changed_by", nullable = false)
    private String changedBy;

    @Column(name = "before_state_json")
    private String beforeStateJson;

    @Column(name = "after_state_json")
    private String afterStateJson;

    @Column(nullable = false)
    private Instant timestamp;

    protected AuditLogJpaEntity() {}

    public AuditLogJpaEntity(UUID id, UUID projectId, String flagKey, String environmentKey,
                             String action, String changedBy, String beforeStateJson,
                             String afterStateJson, Instant timestamp) {
        this.id = id;
        this.projectId = projectId;
        this.flagKey = flagKey;
        this.environmentKey = environmentKey;
        this.action = action;
        this.changedBy = changedBy;
        this.beforeStateJson = beforeStateJson;
        this.afterStateJson = afterStateJson;
        this.timestamp = timestamp;
    }

    public UUID getId() { return id; }
    public UUID getProjectId() { return projectId; }
    public String getFlagKey() { return flagKey; }
    public String getEnvironmentKey() { return environmentKey; }
    public String getAction() { return action; }
    public String getChangedBy() { return changedBy; }
    public String getBeforeStateJson() { return beforeStateJson; }
    public String getAfterStateJson() { return afterStateJson; }
    public Instant getTimestamp() { return timestamp; }
}
