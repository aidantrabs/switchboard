package com.switchboard.application.service;

import com.switchboard.application.port.input.FlagEvaluationUseCase;
import com.switchboard.application.port.output.FlagPersistencePort;
import com.switchboard.application.port.output.ProjectPersistencePort;
import com.switchboard.domain.exception.FlagNotFoundException;
import com.switchboard.domain.exception.ProjectNotFoundException;
import com.switchboard.domain.model.*;
import com.switchboard.domain.service.FlagEvaluationEngine;

import java.util.List;

public class FlagEvaluationService implements FlagEvaluationUseCase {

    private final FlagPersistencePort flagPersistence;
    private final ProjectPersistencePort projectPersistence;

    public FlagEvaluationService(FlagPersistencePort flagPersistence,
                                 ProjectPersistencePort projectPersistence) {
        this.flagPersistence = flagPersistence;
        this.projectPersistence = projectPersistence;
    }

    @Override
    public EvaluationResult evaluate(String projectKey, String environmentKey,
                                     String flagKey, EvaluationContext context) {
        Project project = projectPersistence.findByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        Environment env = projectPersistence.findEnvironmentByProjectIdAndKey(
                project.getId(), environmentKey)
            .orElseThrow(() -> new IllegalArgumentException("environment not found: " + environmentKey));

        FeatureFlag flag = flagPersistence.findByProjectIdAndKey(project.getId(), flagKey)
            .orElseThrow(() -> new FlagNotFoundException(flagKey));

        FlagEnvironmentConfig config = flagPersistence.findConfig(flag.getId(), env.getId())
            .orElse(FlagEnvironmentConfig.create(flag.getId(), env.getId()));

        return FlagEvaluationEngine.evaluate(flag, config, context);
    }

    @Override
    public List<FlagWithConfig> getAllFlags(String projectKey, String environmentKey) {
        Project project = projectPersistence.findByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        Environment env = projectPersistence.findEnvironmentByProjectIdAndKey(
                project.getId(), environmentKey)
            .orElseThrow(() -> new IllegalArgumentException("environment not found: " + environmentKey));

        List<FeatureFlag> flags = flagPersistence.findAllByProjectId(project.getId());

        return flags.stream().map(flag -> {
            FlagEnvironmentConfig config = flagPersistence.findConfig(flag.getId(), env.getId())
                .orElse(FlagEnvironmentConfig.create(flag.getId(), env.getId()));
            return new FlagWithConfig(flag, config);
        }).toList();
    }
}
