package com.switchboard.adapter.output.persistence;

import com.switchboard.adapter.output.persistence.mapper.FlagPersistenceMapper;
import com.switchboard.adapter.output.persistence.repository.FeatureFlagJpaRepository;
import com.switchboard.adapter.output.persistence.repository.FlagEnvironmentConfigJpaRepository;
import com.switchboard.application.port.output.FlagPersistencePort;
import com.switchboard.domain.model.FeatureFlag;
import com.switchboard.domain.model.FlagEnvironmentConfig;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class FlagPersistenceAdapter implements FlagPersistencePort {

    private final FeatureFlagJpaRepository flagRepository;
    private final FlagEnvironmentConfigJpaRepository configRepository;

    public FlagPersistenceAdapter(FeatureFlagJpaRepository flagRepository,
                                  FlagEnvironmentConfigJpaRepository configRepository) {
        this.flagRepository = flagRepository;
        this.configRepository = configRepository;
    }

    @Override
    public FeatureFlag save(FeatureFlag flag) {
        var entity = FlagPersistenceMapper.toJpaEntity(flag);
        var saved = flagRepository.save(entity);
        return FlagPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<FeatureFlag> findByProjectIdAndKey(UUID projectId, String flagKey) {
        return flagRepository.findByProjectIdAndKey(projectId, flagKey)
            .map(FlagPersistenceMapper::toDomain);
    }

    @Override
    public List<FeatureFlag> findAllByProjectId(UUID projectId) {
        return flagRepository.findAllByProjectId(projectId).stream()
            .map(FlagPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public void delete(UUID flagId) {
        flagRepository.deleteById(flagId);
    }

    @Override
    public boolean existsByProjectIdAndKey(UUID projectId, String flagKey) {
        return flagRepository.existsByProjectIdAndKey(projectId, flagKey);
    }

    @Override
    public FlagEnvironmentConfig saveConfig(FlagEnvironmentConfig config) {
        var entity = FlagPersistenceMapper.toJpaEntity(config);
        var saved = configRepository.save(entity);
        return FlagPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<FlagEnvironmentConfig> findConfig(UUID flagId, UUID environmentId) {
        return configRepository.findByFlagIdAndEnvironmentId(flagId, environmentId)
            .map(FlagPersistenceMapper::toDomain);
    }

    @Override
    public List<FlagEnvironmentConfig> findAllConfigsByEnvironment(UUID projectId, UUID environmentId) {
        return configRepository.findAllByProjectIdAndEnvironmentId(projectId, environmentId).stream()
            .map(FlagPersistenceMapper::toDomain)
            .toList();
    }
}
