package com.switchboard.adapter.output.persistence;

import com.switchboard.adapter.output.persistence.entity.AuditLogJpaEntity;
import com.switchboard.adapter.output.persistence.repository.AuditLogJpaRepository;
import com.switchboard.application.port.output.AuditLogPersistencePort;
import com.switchboard.domain.model.AuditLog;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class AuditLogPersistenceAdapter implements AuditLogPersistencePort {

    private final AuditLogJpaRepository repository;

    public AuditLogPersistenceAdapter(AuditLogJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public AuditLog save(AuditLog auditLog) {
        var entity = toJpaEntity(auditLog);
        var saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<AuditLog> findByProjectId(UUID projectId, Instant from, Instant to) {
        return repository.findByProjectIdAndTimestampBetweenOrderByTimestampDesc(projectId, from, to)
            .stream()
            .map(this::toDomain)
            .toList();
    }

    @Override
    public List<AuditLog> findByProjectIdAndFlagKey(UUID projectId, String flagKey, int limit) {
        return repository.findByProjectIdAndFlagKeyOrderByTimestampDesc(
                projectId, flagKey, PageRequest.of(0, limit))
            .stream()
            .map(this::toDomain)
            .toList();
    }

    private AuditLogJpaEntity toJpaEntity(AuditLog log) {
        return new AuditLogJpaEntity(
            log.getId(), log.getProjectId(), log.getFlagKey(), log.getEnvironmentKey(),
            log.getAction(), log.getChangedBy(), log.getBeforeState(), log.getAfterState(),
            log.getTimestamp()
        );
    }

    private AuditLog toDomain(AuditLogJpaEntity entity) {
        return new AuditLog(
            entity.getId(), entity.getProjectId(), entity.getFlagKey(),
            entity.getEnvironmentKey(), entity.getAction(), entity.getChangedBy(),
            entity.getBeforeStateJson(), entity.getAfterStateJson(), entity.getTimestamp()
        );
    }
}
