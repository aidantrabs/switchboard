package com.switchboard.adapter.output.persistence.repository;

import com.switchboard.adapter.output.persistence.entity.ProjectJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectJpaRepository extends JpaRepository<ProjectJpaEntity, UUID> {

    Optional<ProjectJpaEntity> findByKey(String key);

    List<ProjectJpaEntity> findAllByOrganizationId(UUID organizationId);
}
