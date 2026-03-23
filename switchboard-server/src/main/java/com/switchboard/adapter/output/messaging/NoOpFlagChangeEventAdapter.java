package com.switchboard.adapter.output.messaging;

import com.switchboard.application.port.output.FlagChangeEventPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(KafkaFlagChangeEventAdapter.class)
public class NoOpFlagChangeEventAdapter implements FlagChangeEventPort {

    @Override
    public void publishFlagCreated(String projectKey, String environmentKey,
                                   String flagKey, String changedBy) {}

    @Override
    public void publishFlagUpdated(String projectKey, String environmentKey,
                                   String flagKey, String changedBy) {}

    @Override
    public void publishFlagDeleted(String projectKey, String environmentKey,
                                   String flagKey, String changedBy) {}

    @Override
    public void publishFlagToggled(String projectKey, String environmentKey,
                                   String flagKey, boolean enabled, String changedBy) {}

    @Override
    public void publishRolloutUpdated(String projectKey, String environmentKey,
                                      String flagKey, int percentage, String changedBy) {}

    @Override
    public void publishTargetingUpdated(String projectKey, String environmentKey,
                                        String flagKey, String changedBy) {}
}
