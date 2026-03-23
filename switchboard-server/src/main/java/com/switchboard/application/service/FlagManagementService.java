package com.switchboard.application.service;

import com.switchboard.application.port.input.FlagManagementUseCase;
import com.switchboard.application.port.output.AuditLogPersistencePort;
import com.switchboard.application.port.output.FlagChangeEventPort;
import com.switchboard.application.port.output.FlagPersistencePort;
import com.switchboard.application.port.output.ProjectPersistencePort;
import com.switchboard.domain.exception.DuplicateFlagKeyException;
import com.switchboard.domain.exception.FlagNotFoundException;
import com.switchboard.domain.exception.ProjectNotFoundException;
import com.switchboard.domain.model.*;
import com.switchboard.domain.service.FlagStateSerializer;

import java.util.List;

public class FlagManagementService implements FlagManagementUseCase {

    private final FlagPersistencePort flagPersistence;
    private final ProjectPersistencePort projectPersistence;
    private final AuditLogPersistencePort auditLogPersistence;
    private final FlagChangeEventPort flagChangeEvent;

    public FlagManagementService(FlagPersistencePort flagPersistence,
                                 ProjectPersistencePort projectPersistence,
                                 AuditLogPersistencePort auditLogPersistence,
                                 FlagChangeEventPort flagChangeEvent) {
        this.flagPersistence = flagPersistence;
        this.projectPersistence = projectPersistence;
        this.auditLogPersistence = auditLogPersistence;
        this.flagChangeEvent = flagChangeEvent;
    }

    @Override
    public List<FeatureFlag> listFlags(String projectKey) {
        Project project = projectPersistence.findByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        return flagPersistence.findAllByProjectId(project.getId());
    }

    @Override
    public FeatureFlag getFlag(String projectKey, String flagKey) {
        Project project = projectPersistence.findByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        return flagPersistence.findByProjectIdAndKey(project.getId(), flagKey)
            .orElseThrow(() -> new FlagNotFoundException(flagKey));
    }

    @Override
    public FeatureFlag createFlag(String projectKey, String key, String name, String description,
                                  FlagType flagType, String defaultVariant, List<Variant> variants) {
        Project project = projectPersistence.findByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        if (flagPersistence.existsByProjectIdAndKey(project.getId(), key)) {
            throw new DuplicateFlagKeyException(key);
        }

        FeatureFlag flag = FeatureFlag.create(project.getId(), key, name, description,
            flagType, defaultVariant, variants);
        FeatureFlag saved = flagPersistence.save(flag);

        String afterState = FlagStateSerializer.serializeFlag(saved);
        auditLogPersistence.save(AuditLog.create(
            project.getId(), key, null, "FLAG_CREATED", "system", null, afterState));

        flagChangeEvent.publishFlagCreated(projectKey, null, key, "system");

        return saved;
    }

    @Override
    public FeatureFlag updateFlag(String projectKey, String flagKey, String name,
                                  String description, String defaultVariant, List<Variant> variants) {
        Project project = projectPersistence.findByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        FeatureFlag flag = flagPersistence.findByProjectIdAndKey(project.getId(), flagKey)
            .orElseThrow(() -> new FlagNotFoundException(flagKey));

        String beforeState = FlagStateSerializer.serializeFlag(flag);
        flag.update(name, description, defaultVariant, variants);
        FeatureFlag saved = flagPersistence.save(flag);
        String afterState = FlagStateSerializer.serializeFlag(saved);

        auditLogPersistence.save(AuditLog.create(
            project.getId(), flagKey, null, "FLAG_UPDATED", "system", beforeState, afterState));

        flagChangeEvent.publishFlagUpdated(projectKey, null, flagKey, "system");

        return saved;
    }

    @Override
    public void deleteFlag(String projectKey, String flagKey) {
        Project project = projectPersistence.findByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        FeatureFlag flag = flagPersistence.findByProjectIdAndKey(project.getId(), flagKey)
            .orElseThrow(() -> new FlagNotFoundException(flagKey));

        String beforeState = FlagStateSerializer.serializeFlag(flag);
        flagPersistence.delete(flag.getId());

        auditLogPersistence.save(AuditLog.create(
            project.getId(), flagKey, null, "FLAG_DELETED", "system", beforeState, null));

        flagChangeEvent.publishFlagDeleted(projectKey, null, flagKey, "system");
    }

    @Override
    public void toggleFlag(String projectKey, String flagKey, String environmentKey) {
        Project project = projectPersistence.findByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        FeatureFlag flag = flagPersistence.findByProjectIdAndKey(project.getId(), flagKey)
            .orElseThrow(() -> new FlagNotFoundException(flagKey));

        Environment env = projectPersistence.findEnvironmentByProjectIdAndKey(
                project.getId(), environmentKey)
            .orElseThrow(() -> new IllegalArgumentException("environment not found: " + environmentKey));

        FlagEnvironmentConfig config = flagPersistence.findConfig(flag.getId(), env.getId())
            .orElse(FlagEnvironmentConfig.create(flag.getId(), env.getId()));

        String beforeState = FlagStateSerializer.serializeConfig(config);
        config.toggle();
        flagPersistence.saveConfig(config);
        String afterState = FlagStateSerializer.serializeConfig(config);

        auditLogPersistence.save(AuditLog.create(
            project.getId(), flagKey, environmentKey, "FLAG_TOGGLED", "system",
            beforeState, afterState));

        flagChangeEvent.publishFlagToggled(projectKey, environmentKey, flagKey,
            config.isEnabled(), "system");
    }

    @Override
    public void updateRollout(String projectKey, String flagKey, String environmentKey,
                              int percentage) {
        Project project = projectPersistence.findByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        FeatureFlag flag = flagPersistence.findByProjectIdAndKey(project.getId(), flagKey)
            .orElseThrow(() -> new FlagNotFoundException(flagKey));

        Environment env = projectPersistence.findEnvironmentByProjectIdAndKey(
                project.getId(), environmentKey)
            .orElseThrow(() -> new IllegalArgumentException("environment not found: " + environmentKey));

        FlagEnvironmentConfig config = flagPersistence.findConfig(flag.getId(), env.getId())
            .orElse(FlagEnvironmentConfig.create(flag.getId(), env.getId()));

        String beforeState = FlagStateSerializer.serializeConfig(config);
        config.updateRolloutPercentage(percentage);
        flagPersistence.saveConfig(config);
        String afterState = FlagStateSerializer.serializeConfig(config);

        auditLogPersistence.save(AuditLog.create(
            project.getId(), flagKey, environmentKey, "ROLLOUT_UPDATED", "system",
            beforeState, afterState));

        flagChangeEvent.publishRolloutUpdated(projectKey, environmentKey, flagKey,
            percentage, "system");
    }

    @Override
    public void updateTargeting(String projectKey, String flagKey, String environmentKey,
                                List<TargetingRule> rules) {
        Project project = projectPersistence.findByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        FeatureFlag flag = flagPersistence.findByProjectIdAndKey(project.getId(), flagKey)
            .orElseThrow(() -> new FlagNotFoundException(flagKey));

        Environment env = projectPersistence.findEnvironmentByProjectIdAndKey(
                project.getId(), environmentKey)
            .orElseThrow(() -> new IllegalArgumentException("environment not found: " + environmentKey));

        FlagEnvironmentConfig config = flagPersistence.findConfig(flag.getId(), env.getId())
            .orElse(FlagEnvironmentConfig.create(flag.getId(), env.getId()));

        String beforeState = FlagStateSerializer.serializeConfig(config);
        config.updateTargetingRules(rules);
        flagPersistence.saveConfig(config);
        String afterState = FlagStateSerializer.serializeConfig(config);

        auditLogPersistence.save(AuditLog.create(
            project.getId(), flagKey, environmentKey, "TARGETING_UPDATED", "system",
            beforeState, afterState));

        flagChangeEvent.publishTargetingUpdated(projectKey, environmentKey, flagKey, "system");
    }
}
