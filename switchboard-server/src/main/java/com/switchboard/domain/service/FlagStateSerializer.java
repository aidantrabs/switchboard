package com.switchboard.domain.service;

import com.switchboard.domain.model.FeatureFlag;
import com.switchboard.domain.model.FlagEnvironmentConfig;

import java.util.StringJoiner;

public final class FlagStateSerializer {

    private FlagStateSerializer() {}

    public static String serializeFlag(FeatureFlag flag) {
        if (flag == null) return null;

        StringJoiner sj = new StringJoiner(",", "{", "}");
        sj.add("\"key\":\"" + flag.getKey() + "\"");
        sj.add("\"name\":\"" + flag.getName() + "\"");
        sj.add("\"flagType\":\"" + flag.getFlagType().name() + "\"");
        sj.add("\"defaultVariant\":\"" + flag.getDefaultVariant() + "\"");

        StringJoiner variants = new StringJoiner(",", "[", "]");
        flag.getVariants().forEach(v ->
            variants.add("{\"key\":\"" + v.key() + "\",\"value\":\"" + v.value() + "\"}"));
        sj.add("\"variants\":" + variants);

        return sj.toString();
    }

    public static String serializeConfig(FlagEnvironmentConfig config) {
        if (config == null) return null;

        StringJoiner sj = new StringJoiner(",", "{", "}");
        sj.add("\"enabled\":" + config.isEnabled());
        sj.add("\"rolloutPercentage\":" + config.getRolloutPercentage());
        sj.add("\"targetingRuleCount\":" + config.getTargetingRules().size());

        return sj.toString();
    }
}
