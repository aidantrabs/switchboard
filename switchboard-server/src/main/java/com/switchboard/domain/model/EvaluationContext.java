package com.switchboard.domain.model;

import java.util.Map;

public record EvaluationContext(String userId, Map<String, String> attributes) {

    public EvaluationContext(String userId, Map<String, String> attributes) {
        this.userId = userId;
        this.attributes = Map.copyOf(attributes);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }
}
