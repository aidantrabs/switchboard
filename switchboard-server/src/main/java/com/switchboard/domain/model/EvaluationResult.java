package com.switchboard.domain.model;

public record EvaluationResult(String flagKey, String variantKey, String value, Reason reason) {

    public enum Reason {
        FLAG_DISABLED,
        TARGETING_MATCH,
        ROLLOUT,
        DEFAULT
    }
}
