package com.switchboard.domain.model;

import java.util.List;
import java.util.UUID;

public class TargetingRule {

    private final UUID id;
    private final int priority;
    private final List<Condition> conditions;
    private final String servedVariantKey;

    public TargetingRule(UUID id, int priority, List<Condition> conditions, String servedVariantKey) {
        this.id = id;
        this.priority = priority;
        this.conditions = List.copyOf(conditions);
        this.servedVariantKey = servedVariantKey;
    }

    public static TargetingRule create(int priority, List<Condition> conditions, String servedVariantKey) {
        return new TargetingRule(UUID.randomUUID(), priority, conditions, servedVariantKey);
    }

    public UUID getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public String getServedVariantKey() {
        return servedVariantKey;
    }
}
