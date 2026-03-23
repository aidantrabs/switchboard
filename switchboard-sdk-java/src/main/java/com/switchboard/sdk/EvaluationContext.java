package com.switchboard.sdk;

import java.util.HashMap;
import java.util.Map;

public class EvaluationContext {

    private final String userId;
    private final Map<String, String> attributes;

    private EvaluationContext(String userId, Map<String, String> attributes) {
        this.userId = userId;
        this.attributes = Map.copyOf(attributes);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getUserId() {
        return userId;
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public static class Builder {
        private String userId;
        private final Map<String, String> attributes = new HashMap<>();

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder attribute(String key, String value) {
            this.attributes.put(key, value);
            return this;
        }

        public EvaluationContext build() {
            return new EvaluationContext(userId, attributes);
        }
    }
}
