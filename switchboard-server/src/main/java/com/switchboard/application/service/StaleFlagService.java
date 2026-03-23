package com.switchboard.application.service;

import com.switchboard.application.port.input.StaleFlagUseCase;
import com.switchboard.application.port.output.FlagPersistencePort;
import com.switchboard.application.port.output.ProjectPersistencePort;
import com.switchboard.domain.exception.ProjectNotFoundException;
import com.switchboard.domain.model.FeatureFlag;
import com.switchboard.domain.model.Project;

import java.util.List;

public class StaleFlagService implements StaleFlagUseCase {

    private final FlagPersistencePort flagPersistence;
    private final ProjectPersistencePort projectPersistence;

    public StaleFlagService(FlagPersistencePort flagPersistence,
                            ProjectPersistencePort projectPersistence) {
        this.flagPersistence = flagPersistence;
        this.projectPersistence = projectPersistence;
    }

    @Override
    public List<FeatureFlag> findStaleFlags(String projectKey) {
        Project project = projectPersistence.findByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        return flagPersistence.findAllByProjectId(project.getId()).stream()
            .filter(FeatureFlag::isStale)
            .toList();
    }
}
