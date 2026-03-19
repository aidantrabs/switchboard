package com.switchboard.application.port.input;

import com.switchboard.domain.model.AuditLog;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AuditLogQueryUseCase {

    List<AuditLog> queryByProject(UUID projectId, Instant from, Instant to);

    List<AuditLog> queryByFlag(UUID projectId, String flagKey, int limit);
}
