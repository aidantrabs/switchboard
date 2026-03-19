package com.switchboard.application.port.output;

import java.util.UUID;

public interface FlagChangeEventPort {

    void publishFlagCreated(String projectKey, String environmentKey, String flagKey, String changedBy);

    void publishFlagUpdated(String projectKey, String environmentKey, String flagKey, String changedBy);

    void publishFlagDeleted(String projectKey, String environmentKey, String flagKey, String changedBy);

    void publishFlagToggled(String projectKey, String environmentKey, String flagKey,
                            boolean enabled, String changedBy);

    void publishRolloutUpdated(String projectKey, String environmentKey, String flagKey,
                               int percentage, String changedBy);

    void publishTargetingUpdated(String projectKey, String environmentKey, String flagKey,
                                 String changedBy);
}
