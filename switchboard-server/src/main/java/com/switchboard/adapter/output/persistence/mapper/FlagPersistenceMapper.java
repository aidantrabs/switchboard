package com.switchboard.adapter.output.persistence.mapper;

import com.switchboard.adapter.output.persistence.entity.*;
import com.switchboard.domain.model.*;

import java.time.Duration;
import java.util.List;

public final class FlagPersistenceMapper {

    private FlagPersistenceMapper() {}

    public static FeatureFlagJpaEntity toJpaEntity(FeatureFlag flag) {
        Long staleAfterSeconds = flag.getStaleAfter() != null ? flag.getStaleAfter().getSeconds() : null;
        return new FeatureFlagJpaEntity(
            flag.getId(), flag.getProjectId(), flag.getKey(), flag.getName(),
            flag.getDescription(), flag.getFlagType().name(), flag.getDefaultVariant(),
            staleAfterSeconds, flag.getCreatedAt(), flag.getUpdatedAt()
        );
    }

    public static FeatureFlag toDomain(FeatureFlagJpaEntity entity, List<VariantJpaEntity> variantEntities) {
        Duration staleAfter = entity.getStaleAfter() != null
            ? Duration.ofSeconds(entity.getStaleAfter()) : null;

        List<Variant> variants = variantEntities.stream()
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
        return new FlagEnvironmentConfigJpaEntity(
            config.getId(), config.getFlagId(), config.getEnvironmentId(),
            config.isEnabled(), config.getRolloutPercentage(), config.getUpdatedAt()
        );
    }

    public static FlagEnvironmentConfig toDomain(FlagEnvironmentConfigJpaEntity entity,
                                                 List<TargetingRuleJpaEntity> ruleEntities) {
        List<TargetingRule> rules = ruleEntities.stream()
            .map(FlagPersistenceMapper::toDomain)
            .toList();

        return new FlagEnvironmentConfig(
            entity.getId(), entity.getFlagId(), entity.getEnvironmentId(),
            entity.isEnabled(), entity.getRolloutPercentage(), rules,
            entity.getUpdatedAt()
        );
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
