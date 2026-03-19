package com.switchboard.application.service;

import com.switchboard.application.port.input.AuditLogQueryUseCase;
import com.switchboard.application.port.output.AuditLogPersistencePort;
import com.switchboard.domain.model.AuditLog;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class AuditLogQueryService implements AuditLogQueryUseCase {

    private final AuditLogPersistencePort auditLogPersistence;

    public AuditLogQueryService(AuditLogPersistencePort auditLogPersistence) {
        this.auditLogPersistence = auditLogPersistence;
    }

    @Override
    public List<AuditLog> queryByProject(UUID projectId, Instant from, Instant to) {
        return auditLogPersistence.findByProjectId(projectId, from, to);
    }

    @Override
    public List<AuditLog> queryByFlag(UUID projectId, String flagKey, int limit) {
        return auditLogPersistence.findByProjectIdAndFlagKey(projectId, flagKey, limit);
    }
}
