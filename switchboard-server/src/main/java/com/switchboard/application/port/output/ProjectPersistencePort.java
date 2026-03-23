package com.switchboard.application.port.output;

import com.switchboard.domain.model.Environment;
import com.switchboard.domain.model.Project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectPersistencePort {

    Project save(Project project);

    Optional<Project> findByKey(String projectKey);

    List<Project> findAllByOrganizationId(UUID organizationId);

    List<Environment> findEnvironmentsByProjectId(UUID projectId);

    Optional<Environment> findEnvironmentByProjectIdAndKey(UUID projectId, String environmentKey);

    Environment saveEnvironment(Environment environment);

    List<Project> findAll();
}
