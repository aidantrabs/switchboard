package com.switchboard.adapter.output.messaging;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record FlagChangeEvent(
    String eventId,
    String eventType,
    String projectKey,
    String environmentKey,
    String flagKey,
    Instant timestamp,
    String changedBy,
    Map<String, Object> payload
) {

    public static FlagChangeEvent of(String eventType, String projectKey, String environmentKey,
                                     String flagKey, String changedBy, Map<String, Object> payload) {
        return new FlagChangeEvent(
            UUID.randomUUID().toString(), eventType, projectKey, environmentKey,
            flagKey, Instant.now(), changedBy, payload != null ? payload : Map.of()
        );
    }
}
