package com.switchboard.application.port.input;

import com.switchboard.domain.model.Environment;
import com.switchboard.domain.model.Project;

import java.util.List;
import java.util.UUID;

public interface ProjectManagementUseCase {

    List<Project> listAllProjects();

    List<Project> listProjects(UUID organizationId);

    Project getProject(String projectKey);

    Project createProject(UUID organizationId, String name, String key);

    List<Environment> listEnvironments(String projectKey);

    Environment createEnvironment(String projectKey, String name, String key, int sortOrder);
}
