package com.switchboard.adapter.output.persistence.repository;

import com.switchboard.adapter.output.persistence.entity.AuditLogJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AuditLogJpaRepository extends JpaRepository<AuditLogJpaEntity, UUID> {

    List<AuditLogJpaEntity> findByProjectIdAndTimestampBetweenOrderByTimestampDesc(
        UUID projectId, Instant from, Instant to);

    List<AuditLogJpaEntity> findByProjectIdAndFlagKeyOrderByTimestampDesc(
        UUID projectId, String flagKey, Pageable pageable);
}
