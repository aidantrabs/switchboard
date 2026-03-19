package com.switchboard.application.port.output;

import com.switchboard.domain.model.AuditLog;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AuditLogPersistencePort {

    AuditLog save(AuditLog auditLog);

    List<AuditLog> findByProjectId(UUID projectId, Instant from, Instant to);

    List<AuditLog> findByProjectIdAndFlagKey(UUID projectId, String flagKey, int limit);
}
