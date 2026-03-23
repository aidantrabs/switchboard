package com.switchboard.adapter.output.messaging;

import com.switchboard.application.port.output.FlagChangeEventPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaFlagChangeEventAdapter implements FlagChangeEventPort {

    private static final String TOPIC_PREFIX = "switchboard.flag-events.";

    private final KafkaTemplate<String, FlagChangeEvent> kafkaTemplate;

    public KafkaFlagChangeEventAdapter(KafkaTemplate<String, FlagChangeEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishFlagCreated(String projectKey, String environmentKey,
                                   String flagKey, String changedBy) {
        publish("FLAG_CREATED", projectKey, environmentKey, flagKey, changedBy, null);
    }

    @Override
    public void publishFlagUpdated(String projectKey, String environmentKey,
                                   String flagKey, String changedBy) {
        publish("FLAG_UPDATED", projectKey, environmentKey, flagKey, changedBy, null);
    }

    @Override
    public void publishFlagDeleted(String projectKey, String environmentKey,
                                   String flagKey, String changedBy) {
        publish("FLAG_DELETED", projectKey, environmentKey, flagKey, changedBy, null);
    }

    @Override
    public void publishFlagToggled(String projectKey, String environmentKey,
                                   String flagKey, boolean enabled, String changedBy) {
        publish("FLAG_TOGGLED", projectKey, environmentKey, flagKey, changedBy,
            Map.of("enabled", enabled));
    }

    @Override
    public void publishRolloutUpdated(String projectKey, String environmentKey,
                                      String flagKey, int percentage, String changedBy) {
        publish("ROLLOUT_UPDATED", projectKey, environmentKey, flagKey, changedBy,
            Map.of("rolloutPercentage", percentage));
    }

    @Override
    public void publishTargetingUpdated(String projectKey, String environmentKey,
                                        String flagKey, String changedBy) {
        publish("TARGETING_UPDATED", projectKey, environmentKey, flagKey, changedBy, null);
    }

    private void publish(String eventType, String projectKey, String environmentKey,
                         String flagKey, String changedBy, Map<String, Object> payload) {
        String topic = buildTopic(projectKey, environmentKey);
        FlagChangeEvent event = FlagChangeEvent.of(eventType, projectKey, environmentKey,
            flagKey, changedBy, payload);
        kafkaTemplate.send(topic, flagKey, event);
    }

    private String buildTopic(String projectKey, String environmentKey) {
        if (environmentKey != null) {
            return TOPIC_PREFIX + projectKey + "." + environmentKey;
        }
        return TOPIC_PREFIX + projectKey;
    }
}
