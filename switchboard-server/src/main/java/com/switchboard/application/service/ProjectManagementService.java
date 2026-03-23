package com.switchboard.application.service;

import com.switchboard.application.port.input.ProjectManagementUseCase;
import com.switchboard.application.port.output.ProjectPersistencePort;
import com.switchboard.domain.exception.ProjectNotFoundException;
import com.switchboard.domain.model.Environment;
import com.switchboard.domain.model.Project;

import java.util.List;
import java.util.UUID;

public class ProjectManagementService implements ProjectManagementUseCase {

    private final ProjectPersistencePort projectPersistence;

    public ProjectManagementService(ProjectPersistencePort projectPersistence) {
        this.projectPersistence = projectPersistence;
    }

    @Override
    public List<Project> listAllProjects() {
        return projectPersistence.findAll();
    }

    @Override
    public List<Project> listProjects(UUID organizationId) {
        return projectPersistence.findAllByOrganizationId(organizationId);
    }

    @Override
    public Project getProject(String projectKey) {
        return projectPersistence.findByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));
    }

    @Override
    public Project createProject(UUID organizationId, String name, String key) {
        Project project = Project.create(organizationId, name, key);
        return projectPersistence.save(project);
    }

    @Override
    public List<Environment> listEnvironments(String projectKey) {
        Project project = projectPersistence.findByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        return projectPersistence.findEnvironmentsByProjectId(project.getId());
    }

    @Override
    public Environment createEnvironment(String projectKey, String name, String key, int sortOrder) {
        Project project = projectPersistence.findByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        Environment environment = Environment.create(project.getId(), name, key, sortOrder);
        return projectPersistence.saveEnvironment(environment);
    }
}
