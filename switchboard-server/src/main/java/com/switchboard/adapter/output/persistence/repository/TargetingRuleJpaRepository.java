package com.switchboard.adapter.output.persistence.repository;

import com.switchboard.adapter.output.persistence.entity.TargetingRuleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TargetingRuleJpaRepository extends JpaRepository<TargetingRuleJpaEntity, UUID> {

    List<TargetingRuleJpaEntity> findAllByFlagEnvConfigIdOrderByPriorityAsc(UUID flagEnvConfigId);

    void deleteAllByFlagEnvConfigId(UUID flagEnvConfigId);
}
