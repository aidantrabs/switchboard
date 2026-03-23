package com.switchboard.adapter.input.rest;

import com.switchboard.adapter.input.rest.dto.EnvironmentResponse;
import com.switchboard.adapter.input.rest.dto.ProjectResponse;
import com.switchboard.application.port.input.ProjectManagementUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectManagementUseCase projectManagement;

    public ProjectController(ProjectManagementUseCase projectManagement) {
        this.projectManagement = projectManagement;
    }

    @GetMapping("/{projectKey}")
    public ProjectResponse getProject(@PathVariable String projectKey) {
        return ProjectResponse.from(projectManagement.getProject(projectKey));
    }

    @GetMapping("/{projectKey}/environments")
    public List<EnvironmentResponse> listEnvironments(@PathVariable String projectKey) {
        return projectManagement.listEnvironments(projectKey).stream()
            .map(EnvironmentResponse::from)
            .toList();
    }
}
