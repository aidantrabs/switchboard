package com.switchboard.application.port.input;

import com.switchboard.domain.model.FeatureFlag;
import com.switchboard.domain.model.FlagType;
import com.switchboard.domain.model.TargetingRule;
import com.switchboard.domain.model.Variant;

import java.util.List;

public interface FlagManagementUseCase {

    List<FeatureFlag> listFlags(String projectKey);

    FeatureFlag getFlag(String projectKey, String flagKey);

    FeatureFlag createFlag(String projectKey, String key, String name, String description,
                           FlagType flagType, String defaultVariant, List<Variant> variants);

    FeatureFlag updateFlag(String projectKey, String flagKey, String name, String description,
                           String defaultVariant, List<Variant> variants);

    void deleteFlag(String projectKey, String flagKey);

    void toggleFlag(String projectKey, String flagKey, String environmentKey);

    void updateRollout(String projectKey, String flagKey, String environmentKey, int percentage);

    void updateTargeting(String projectKey, String flagKey, String environmentKey,
                         List<TargetingRule> rules);
}
