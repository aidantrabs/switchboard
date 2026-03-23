package com.switchboard.sdk.model;

import java.util.List;

public class FlagConfig {

    private String key;
    private boolean enabled;
    private String defaultVariant;
    private List<Variant> variants;
    private int rolloutPercentage;
    private List<TargetingRule> targetingRules;

    public FlagConfig() {}

    public FlagConfig(String key, boolean enabled, String defaultVariant,
                      List<Variant> variants, int rolloutPercentage,
                      List<TargetingRule> targetingRules) {
        this.key = key;
        this.enabled = enabled;
        this.defaultVariant = defaultVariant;
        this.variants = variants;
        this.rolloutPercentage = rolloutPercentage;
        this.targetingRules = targetingRules != null ? targetingRules : List.of();
    }

    public String getKey() { return key; }
    public boolean isEnabled() { return enabled; }
    public String getDefaultVariant() { return defaultVariant; }
    public List<Variant> getVariants() { return variants; }
    public int getRolloutPercentage() { return rolloutPercentage; }
    public List<TargetingRule> getTargetingRules() { return targetingRules; }

    public void setKey(String key) { this.key = key; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setDefaultVariant(String defaultVariant) { this.defaultVariant = defaultVariant; }
    public void setVariants(List<Variant> variants) { this.variants = variants; }
    public void setRolloutPercentage(int rolloutPercentage) { this.rolloutPercentage = rolloutPercentage; }
    public void setTargetingRules(List<TargetingRule> targetingRules) { this.targetingRules = targetingRules; }
}
