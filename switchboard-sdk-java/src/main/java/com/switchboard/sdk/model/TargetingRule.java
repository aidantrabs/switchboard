package com.switchboard.sdk.model;

import java.util.List;

public class TargetingRule {

    private int priority;
    private List<Condition> conditions;
    private String servedVariantKey;

    public TargetingRule() {}

    public TargetingRule(int priority, List<Condition> conditions, String servedVariantKey) {
        this.priority = priority;
        this.conditions = conditions;
        this.servedVariantKey = servedVariantKey;
    }

    public int getPriority() { return priority; }
    public List<Condition> getConditions() { return conditions; }
    public String getServedVariantKey() { return servedVariantKey; }

    public void setPriority(int priority) { this.priority = priority; }
    public void setConditions(List<Condition> conditions) { this.conditions = conditions; }
    public void setServedVariantKey(String servedVariantKey) { this.servedVariantKey = servedVariantKey; }
}
