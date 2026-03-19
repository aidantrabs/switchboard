package com.switchboard.adapter.output.persistence.repository;

import com.switchboard.adapter.output.persistence.entity.EnvironmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnvironmentJpaRepository extends JpaRepository<EnvironmentJpaEntity, UUID> {

    List<EnvironmentJpaEntity> findAllByProjectIdOrderBySortOrder(UUID projectId);

    Optional<EnvironmentJpaEntity> findByProjectIdAndKey(UUID projectId, String key);
}
