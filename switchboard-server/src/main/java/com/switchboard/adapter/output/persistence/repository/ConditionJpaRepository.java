package com.switchboard.adapter.output.persistence.repository;

import com.switchboard.adapter.output.persistence.entity.ConditionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConditionJpaRepository extends JpaRepository<ConditionJpaEntity, UUID> {

    List<ConditionJpaEntity> findAllByTargetingRuleId(UUID targetingRuleId);
}
