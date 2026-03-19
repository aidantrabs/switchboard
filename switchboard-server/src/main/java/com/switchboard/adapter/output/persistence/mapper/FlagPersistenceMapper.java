package com.switchboard.adapter.output.persistence.mapper;

import com.switchboard.adapter.output.persistence.entity.*;
import com.switchboard.domain.model.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class FlagPersistenceMapper {

    private FlagPersistenceMapper() {}

    public static FeatureFlagJpaEntity toJpaEntity(FeatureFlag flag) {
        Long staleAfterSeconds = flag.getStaleAfter() != null ? flag.getStaleAfter().getSeconds() : null;
        FeatureFlagJpaEntity entity = new FeatureFlagJpaEntity(
            flag.getId(), flag.getProjectId(), flag.getKey(), flag.getName(),
            flag.getDescription(), flag.getFlagType().name(), flag.getDefaultVariant(),
            staleAfterSeconds, flag.getCreatedAt(), flag.getUpdatedAt()
        );

        List<VariantJpaEntity> variantEntities = flag.getVariants().stream()
            .map(v -> new VariantJpaEntity(UUID.randomUUID(), flag.getId(), v.key(), v.value()))
            .toList();
        entity.setVariants(variantEntities);

        return entity;
    }

    public static FeatureFlag toDomain(FeatureFlagJpaEntity entity) {
        Duration staleAfter = entity.getStaleAfter() != null
            ? Duration.ofSeconds(entity.getStaleAfter()) : null;

        List<Variant> variants = entity.getVariants().stream()
            .map(v -> new Variant(v.getKey(), v.getValueJson()))
            .toList();

        return new FeatureFlag(
            entity.getId(), entity.getProjectId(), entity.getKey(), entity.getName(),
            entity.getDescription(), FlagType.valueOf(entity.getFlagType()),
            entity.getDefaultVariant(), variants, staleAfter,
            entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }

    public static FlagEnvironmentConfigJpaEntity toJpaEntity(FlagEnvironmentConfig config) {
        FlagEnvironmentConfigJpaEntity entity = new FlagEnvironmentConfigJpaEntity(
            config.getId(), config.getFlagId(), config.getEnvironmentId(),
            config.isEnabled(), config.getRolloutPercentage(), config.getUpdatedAt()
        );

        List<TargetingRuleJpaEntity> ruleEntities = config.getTargetingRules().stream()
            .map(rule -> toJpaEntity(rule, config.getId()))
            .toList();
        entity.setTargetingRules(ruleEntities);

        return entity;
    }

    public static FlagEnvironmentConfig toDomain(FlagEnvironmentConfigJpaEntity entity) {
        List<TargetingRule> rules = entity.getTargetingRules().stream()
            .map(FlagPersistenceMapper::toDomain)
            .toList();

        return new FlagEnvironmentConfig(
            entity.getId(), entity.getFlagId(), entity.getEnvironmentId(),
            entity.isEnabled(), entity.getRolloutPercentage(), rules,
            entity.getUpdatedAt()
        );
    }

    private static TargetingRuleJpaEntity toJpaEntity(TargetingRule rule, UUID configId) {
        TargetingRuleJpaEntity entity = new TargetingRuleJpaEntity(
            rule.getId(), configId, rule.getPriority(),
            rule.getServedVariantKey(), Instant.now()
        );

        List<ConditionJpaEntity> conditionEntities = rule.getConditions().stream()
            .map(c -> new ConditionJpaEntity(
                UUID.randomUUID(), rule.getId(), c.attribute(),
                c.operator().name(), c.value()))
            .toList();
        entity.setConditions(conditionEntities);

        return entity;
    }

    private static TargetingRule toDomain(TargetingRuleJpaEntity entity) {
        List<Condition> conditions = entity.getConditions().stream()
            .map(c -> new Condition(c.getAttribute(), Operator.valueOf(c.getOperator()), c.getValueJson()))
            .toList();

        return new TargetingRule(
            entity.getId(), entity.getPriority(), conditions, entity.getServedVariantKey()
        );
    }
}
