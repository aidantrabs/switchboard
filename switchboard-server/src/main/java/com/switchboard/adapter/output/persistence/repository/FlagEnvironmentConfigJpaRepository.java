package com.switchboard.adapter.output.persistence.repository;

import com.switchboard.adapter.output.persistence.entity.FlagEnvironmentConfigJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlagEnvironmentConfigJpaRepository extends JpaRepository<FlagEnvironmentConfigJpaEntity, UUID> {

    Optional<FlagEnvironmentConfigJpaEntity> findByFlagIdAndEnvironmentId(UUID flagId, UUID environmentId);

    @Query("SELECT c FROM FlagEnvironmentConfigJpaEntity c WHERE c.flagId IN " +
           "(SELECT f.id FROM FeatureFlagJpaEntity f WHERE f.projectId = :projectId) " +
           "AND c.environmentId = :environmentId")
    List<FlagEnvironmentConfigJpaEntity> findAllByProjectIdAndEnvironmentId(
        @Param("projectId") UUID projectId, @Param("environmentId") UUID environmentId);
}
