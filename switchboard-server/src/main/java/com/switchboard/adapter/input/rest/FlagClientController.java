package com.switchboard.adapter.input.rest;

import com.switchboard.adapter.input.rest.dto.EvaluateFlagRequest;
import com.switchboard.adapter.input.rest.dto.EvaluationResponse;
import com.switchboard.adapter.input.rest.dto.SdkFlagResponse;
import com.switchboard.application.port.input.FlagEvaluationUseCase;
import com.switchboard.domain.model.EvaluationContext;
import com.switchboard.domain.model.EvaluationResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client/{projectKey}/{envKey}")
public class FlagClientController {

    private final FlagEvaluationUseCase flagEvaluation;

    public FlagClientController(FlagEvaluationUseCase flagEvaluation) {
        this.flagEvaluation = flagEvaluation;
    }

    @GetMapping("/flags")
    public List<SdkFlagResponse> getAllFlags(@PathVariable String projectKey,
                                            @PathVariable String envKey) {
        return flagEvaluation.getAllFlags(projectKey, envKey).stream()
            .map(fc -> SdkFlagResponse.from(fc.flag(), fc.config()))
            .toList();
    }

    @PostMapping("/evaluate")
    public EvaluationResponse evaluate(@PathVariable String projectKey,
                                       @PathVariable String envKey,
                                       @RequestBody EvaluateFlagRequest request) {
        EvaluationContext context = new EvaluationContext(
            request.userId(),
            request.attributes() != null ? request.attributes() : Map.of()
        );

        EvaluationResult result = flagEvaluation.evaluate(
            projectKey, envKey, request.flagKey(), context);

        return EvaluationResponse.from(result);
    }
}
