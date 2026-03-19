package com.switchboard.application.port.output;

import com.switchboard.domain.model.FeatureFlag;
import com.switchboard.domain.model.FlagEnvironmentConfig;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlagPersistencePort {

    FeatureFlag save(FeatureFlag flag);

    Optional<FeatureFlag> findByProjectIdAndKey(UUID projectId, String flagKey);

    List<FeatureFlag> findAllByProjectId(UUID projectId);

    void delete(UUID flagId);

    boolean existsByProjectIdAndKey(UUID projectId, String flagKey);

    FlagEnvironmentConfig saveConfig(FlagEnvironmentConfig config);

    Optional<FlagEnvironmentConfig> findConfig(UUID flagId, UUID environmentId);

    List<FlagEnvironmentConfig> findAllConfigsByEnvironment(UUID projectId, UUID environmentId);
}
