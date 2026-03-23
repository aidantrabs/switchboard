package com.switchboard.adapter.input.rest;

import com.switchboard.adapter.input.rest.dto.FlagResponse;
import com.switchboard.application.port.input.ProjectManagementUseCase;
import com.switchboard.application.port.output.FlagPersistencePort;
import com.switchboard.domain.model.Project;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectKey}/flags")
public class StaleFlagController {

    private final FlagPersistencePort flagPersistence;
    private final ProjectManagementUseCase projectManagement;

    public StaleFlagController(FlagPersistencePort flagPersistence,
                               ProjectManagementUseCase projectManagement) {
        this.flagPersistence = flagPersistence;
        this.projectManagement = projectManagement;
    }

    @GetMapping("/stale")
    public List<FlagResponse> getStaleFags(@PathVariable String projectKey) {
        Project project = projectManagement.getProject(projectKey);

        return flagPersistence.findAllByProjectId(project.getId()).stream()
            .filter(flag -> flag.isStale())
            .map(FlagResponse::from)
            .toList();
    }
}
