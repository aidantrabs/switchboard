package com.switchboard.adapter.output.persistence.repository;

import com.switchboard.adapter.output.persistence.entity.VariantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VariantJpaRepository extends JpaRepository<VariantJpaEntity, UUID> {

    List<VariantJpaEntity> findAllByFlagId(UUID flagId);

    void deleteAllByFlagId(UUID flagId);
}
