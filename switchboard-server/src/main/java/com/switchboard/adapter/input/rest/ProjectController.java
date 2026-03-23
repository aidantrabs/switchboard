package com.switchboard.adapter.input.rest;

import com.switchboard.adapter.input.rest.dto.*;
import com.switchboard.application.port.input.ProjectManagementUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectManagementUseCase projectManagement;

    public ProjectController(ProjectManagementUseCase projectManagement) {
        this.projectManagement = projectManagement;
    }

    @GetMapping
    public List<ProjectResponse> listProjects() {
        return projectManagement.listAllProjects().stream()
            .map(ProjectResponse::from)
            .toList();
    }

    @GetMapping("/{projectKey}")
    public ProjectResponse getProject(@PathVariable String projectKey) {
        return ProjectResponse.from(projectManagement.getProject(projectKey));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse createProject(@RequestBody CreateProjectRequest request) {
        return ProjectResponse.from(
            projectManagement.createProject(request.organizationId(), request.name(), request.key()));
    }

    @GetMapping("/{projectKey}/environments")
    public List<EnvironmentResponse> listEnvironments(@PathVariable String projectKey) {
        return projectManagement.listEnvironments(projectKey).stream()
            .map(EnvironmentResponse::from)
            .toList();
    }

    @PostMapping("/{projectKey}/environments")
    @ResponseStatus(HttpStatus.CREATED)
    public EnvironmentResponse createEnvironment(@PathVariable String projectKey,
                                                 @RequestBody CreateEnvironmentRequest request) {
        return EnvironmentResponse.from(
            projectManagement.createEnvironment(projectKey, request.name(), request.key(), request.sortOrder()));
    }
}
