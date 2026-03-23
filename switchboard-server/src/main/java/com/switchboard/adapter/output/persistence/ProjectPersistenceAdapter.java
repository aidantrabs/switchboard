package com.switchboard.adapter.output.persistence;

import com.switchboard.adapter.output.persistence.mapper.ProjectPersistenceMapper;
import com.switchboard.adapter.output.persistence.repository.EnvironmentJpaRepository;
import com.switchboard.adapter.output.persistence.repository.ProjectJpaRepository;
import com.switchboard.application.port.output.ProjectPersistencePort;
import com.switchboard.domain.model.Environment;
import com.switchboard.domain.model.Project;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProjectPersistenceAdapter implements ProjectPersistencePort {

    private final ProjectJpaRepository projectRepository;
    private final EnvironmentJpaRepository environmentRepository;

    public ProjectPersistenceAdapter(ProjectJpaRepository projectRepository,
                                     EnvironmentJpaRepository environmentRepository) {
        this.projectRepository = projectRepository;
        this.environmentRepository = environmentRepository;
    }

    @Override
    public Project save(Project project) {
        var entity = ProjectPersistenceMapper.toJpaEntity(project);
        var saved = projectRepository.save(entity);
        return ProjectPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Project> findByKey(String projectKey) {
        return projectRepository.findByKey(projectKey)
            .map(ProjectPersistenceMapper::toDomain);
    }

    @Override
    public List<Project> findAllByOrganizationId(UUID organizationId) {
        return projectRepository.findAllByOrganizationId(organizationId).stream()
            .map(ProjectPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public List<Environment> findEnvironmentsByProjectId(UUID projectId) {
        return environmentRepository.findAllByProjectIdOrderBySortOrder(projectId).stream()
            .map(ProjectPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public Optional<Environment> findEnvironmentByProjectIdAndKey(UUID projectId, String environmentKey) {
        return environmentRepository.findByProjectIdAndKey(projectId, environmentKey)
            .map(ProjectPersistenceMapper::toDomain);
    }

    @Override
    public Environment saveEnvironment(Environment environment) {
        var entity = new com.switchboard.adapter.output.persistence.entity.EnvironmentJpaEntity(
            environment.getId(), environment.getProjectId(),
            environment.getName(), environment.getKey(), environment.getSortOrder());
        var saved = environmentRepository.save(entity);
        return ProjectPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll().stream()
            .map(ProjectPersistenceMapper::toDomain)
            .toList();
    }
}
