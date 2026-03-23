package com.switchboard.application.port.input;

import com.switchboard.domain.model.EvaluationContext;
import com.switchboard.domain.model.EvaluationResult;
import com.switchboard.domain.model.FlagWithConfig;

import java.util.List;

public interface FlagEvaluationUseCase {

    EvaluationResult evaluate(String projectKey, String environmentKey,
                              String flagKey, EvaluationContext context);

    List<FlagWithConfig> getAllFlags(String projectKey, String environmentKey);
}
