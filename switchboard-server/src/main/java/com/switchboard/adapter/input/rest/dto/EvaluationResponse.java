package com.switchboard.adapter.input.rest.dto;

import com.switchboard.domain.model.EvaluationResult;

public record EvaluationResponse(String flagKey, String variant, String value, String reason) {

    public static EvaluationResponse from(EvaluationResult result) {
        return new EvaluationResponse(
            result.flagKey(), result.variantKey(), result.value(), result.reason().name()
        );
    }
}
