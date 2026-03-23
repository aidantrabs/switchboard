package com.switchboard.adapter.input.rest.dto;

import com.switchboard.domain.model.AuditLog;

import java.time.Instant;

public record AuditLogResponse(
    String id,
    String flagKey,
    String environmentKey,
    String action,
    String changedBy,
    String beforeState,
    String afterState,
    Instant timestamp
) {

    public static AuditLogResponse from(AuditLog log) {
        return new AuditLogResponse(
            log.getId().toString(), log.getFlagKey(), log.getEnvironmentKey(),
            log.getAction(), log.getChangedBy(), log.getBeforeState(),
            log.getAfterState(), log.getTimestamp()
        );
    }
}
