package com.switchboard.adapter.output.persistence.repository;

import com.switchboard.adapter.output.persistence.entity.FeatureFlagJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeatureFlagJpaRepository extends JpaRepository<FeatureFlagJpaEntity, UUID> {

    Optional<FeatureFlagJpaEntity> findByProjectIdAndKey(UUID projectId, String key);

    List<FeatureFlagJpaEntity> findAllByProjectId(UUID projectId);

    boolean existsByProjectIdAndKey(UUID projectId, String key);
}
