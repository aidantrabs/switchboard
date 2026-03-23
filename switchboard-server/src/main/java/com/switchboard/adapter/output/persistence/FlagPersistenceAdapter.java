package com.switchboard.adapter.output.persistence;

import com.switchboard.adapter.output.persistence.entity.ConditionJpaEntity;
import com.switchboard.adapter.output.persistence.entity.TargetingRuleJpaEntity;
import com.switchboard.adapter.output.persistence.entity.VariantJpaEntity;
import com.switchboard.adapter.output.persistence.mapper.FlagPersistenceMapper;
import com.switchboard.adapter.output.persistence.repository.*;
import com.switchboard.application.port.output.FlagPersistencePort;
import com.switchboard.domain.model.FeatureFlag;
import com.switchboard.domain.model.FlagEnvironmentConfig;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class FlagPersistenceAdapter implements FlagPersistencePort {

    private final FeatureFlagJpaRepository flagRepository;
    private final FlagEnvironmentConfigJpaRepository configRepository;
    private final VariantJpaRepository variantRepository;
    private final TargetingRuleJpaRepository targetingRuleRepository;
    private final ConditionJpaRepository conditionRepository;

    public FlagPersistenceAdapter(FeatureFlagJpaRepository flagRepository,
                                  FlagEnvironmentConfigJpaRepository configRepository,
                                  VariantJpaRepository variantRepository,
                                  TargetingRuleJpaRepository targetingRuleRepository,
                                  ConditionJpaRepository conditionRepository) {
        this.flagRepository = flagRepository;
        this.configRepository = configRepository;
        this.variantRepository = variantRepository;
        this.targetingRuleRepository = targetingRuleRepository;
        this.conditionRepository = conditionRepository;
    }

    @Override
    @Transactional
    public FeatureFlag save(FeatureFlag flag) {
        var entity = FlagPersistenceMapper.toJpaEntity(flag);
        var saved = flagRepository.save(entity);

        variantRepository.deleteAllByFlagId(saved.getId());
        List<VariantJpaEntity> variantEntities = flag.getVariants().stream()
            .map(v -> new VariantJpaEntity(UUID.randomUUID(), saved.getId(), v.key(), v.value()))
            .toList();
        variantRepository.saveAll(variantEntities);

        return toDomainWithVariants(saved);
    }

    @Override
    public Optional<FeatureFlag> findByProjectIdAndKey(UUID projectId, String flagKey) {
        return flagRepository.findByProjectIdAndKey(projectId, flagKey)
            .map(this::toDomainWithVariants);
    }

    @Override
    public List<FeatureFlag> findAllByProjectId(UUID projectId) {
        return flagRepository.findAllByProjectId(projectId).stream()
            .map(this::toDomainWithVariants)
            .toList();
    }

    @Override
    @Transactional
    public void delete(UUID flagId) {
        variantRepository.deleteAllByFlagId(flagId);
        flagRepository.deleteById(flagId);
    }

    @Override
    public boolean existsByProjectIdAndKey(UUID projectId, String flagKey) {
        return flagRepository.existsByProjectIdAndKey(projectId, flagKey);
    }

    @Override
    @Transactional
    public FlagEnvironmentConfig saveConfig(FlagEnvironmentConfig config) {
        var entity = FlagPersistenceMapper.toJpaEntity(config);
        var saved = configRepository.save(entity);

        targetingRuleRepository.deleteAllByFlagEnvConfigId(saved.getId());
        for (var rule : config.getTargetingRules()) {
            var ruleEntity = new TargetingRuleJpaEntity(
                rule.getId(), saved.getId(), rule.getPriority(),
                rule.getServedVariantKey(), java.time.Instant.now());
            targetingRuleRepository.save(ruleEntity);

            List<ConditionJpaEntity> conditionEntities = rule.getConditions().stream()
                .map(c -> new ConditionJpaEntity(
                    UUID.randomUUID(), ruleEntity.getId(),
                    c.attribute(), c.operator().name(), c.value()))
                .toList();
            conditionRepository.saveAll(conditionEntities);
        }

        return toDomainWithRules(saved);
    }

    @Override
    public Optional<FlagEnvironmentConfig> findConfig(UUID flagId, UUID environmentId) {
        return configRepository.findByFlagIdAndEnvironmentId(flagId, environmentId)
            .map(this::toDomainWithRules);
    }

    @Override
    public List<FlagEnvironmentConfig> findAllConfigsByEnvironment(UUID projectId, UUID environmentId) {
        return configRepository.findAllByProjectIdAndEnvironmentId(projectId, environmentId).stream()
            .map(this::toDomainWithRules)
            .toList();
    }

    private FeatureFlag toDomainWithVariants(com.switchboard.adapter.output.persistence.entity.FeatureFlagJpaEntity entity) {
        var variants = variantRepository.findAllByFlagId(entity.getId());
        return FlagPersistenceMapper.toDomain(entity, variants);
    }

    private FlagEnvironmentConfig toDomainWithRules(com.switchboard.adapter.output.persistence.entity.FlagEnvironmentConfigJpaEntity entity) {
        var ruleEntities = targetingRuleRepository.findAllByFlagEnvConfigIdOrderByPriorityAsc(entity.getId());
        for (var rule : ruleEntities) {
            var conditions = conditionRepository.findAllByTargetingRuleId(rule.getId());
            rule.setConditions(conditions);
        }
        return FlagPersistenceMapper.toDomain(entity, ruleEntities);
    }
}
