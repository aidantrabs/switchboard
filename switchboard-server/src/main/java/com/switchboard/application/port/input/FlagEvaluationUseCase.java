package com.switchboard.application.port.input;

import com.switchboard.domain.model.EvaluationContext;
import com.switchboard.domain.model.EvaluationResult;
import com.switchboard.domain.model.FlagEnvironmentConfig;

import java.util.List;

public interface FlagEvaluationUseCase {

    EvaluationResult evaluate(String projectKey, String environmentKey,
                              String flagKey, EvaluationContext context);

    List<FlagEnvironmentConfig> getAllFlagConfigs(String projectKey, String environmentKey);
}
