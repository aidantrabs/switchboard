package com.switchboard.adapter.input.rest;

import com.switchboard.adapter.input.rest.dto.*;
import com.switchboard.application.port.input.FlagManagementUseCase;
import com.switchboard.domain.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectKey}/flags")
public class FlagManagementController {

    private final FlagManagementUseCase flagManagement;

    public FlagManagementController(FlagManagementUseCase flagManagement) {
        this.flagManagement = flagManagement;
    }

    @GetMapping
    public List<FlagResponse> listFlags(@PathVariable String projectKey) {
        return flagManagement.listFlags(projectKey).stream()
            .map(FlagResponse::from)
            .toList();
    }

    @GetMapping("/{flagKey}")
    public FlagResponse getFlag(@PathVariable String projectKey,
                                @PathVariable String flagKey) {
        return FlagResponse.from(flagManagement.getFlag(projectKey, flagKey));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlagResponse createFlag(@PathVariable String projectKey,
                                   @RequestBody CreateFlagRequest request) {
        List<Variant> variants = request.variants().stream()
            .map(v -> new Variant(v.key(), v.value()))
            .toList();

        FeatureFlag flag = flagManagement.createFlag(
            projectKey, request.key(), request.name(), request.description(),
            FlagType.valueOf(request.flagType()), request.defaultVariant(), variants
        );

        return FlagResponse.from(flag);
    }

    @PutMapping("/{flagKey}")
    public FlagResponse updateFlag(@PathVariable String projectKey,
                                   @PathVariable String flagKey,
                                   @RequestBody UpdateFlagRequest request) {
        List<Variant> variants = request.variants().stream()
            .map(v -> new Variant(v.key(), v.value()))
            .toList();

        FeatureFlag flag = flagManagement.updateFlag(
            projectKey, flagKey, request.name(), request.description(),
            request.defaultVariant(), variants
        );

        return FlagResponse.from(flag);
    }

    @DeleteMapping("/{flagKey}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFlag(@PathVariable String projectKey,
                           @PathVariable String flagKey) {
        flagManagement.deleteFlag(projectKey, flagKey);
    }

    @PatchMapping("/{flagKey}/environments/{envKey}/toggle")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleFlag(@PathVariable String projectKey,
                           @PathVariable String flagKey,
                           @PathVariable String envKey) {
        flagManagement.toggleFlag(projectKey, flagKey, envKey);
    }

    @PutMapping("/{flagKey}/environments/{envKey}/rollout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRollout(@PathVariable String projectKey,
                              @PathVariable String flagKey,
                              @PathVariable String envKey,
                              @RequestBody UpdateRolloutRequest request) {
        flagManagement.updateRollout(projectKey, flagKey, envKey, request.percentage());
    }

    @PutMapping("/{flagKey}/environments/{envKey}/targeting")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTargeting(@PathVariable String projectKey,
                                @PathVariable String flagKey,
                                @PathVariable String envKey,
                                @RequestBody UpdateTargetingRequest request) {
        List<TargetingRule> rules = request.rules().stream()
            .map(r -> TargetingRule.create(
                r.priority(),
                r.conditions().stream()
                    .map(c -> new Condition(c.attribute(), Operator.valueOf(c.operator()), c.value()))
                    .toList(),
                r.servedVariantKey()))
            .toList();

        flagManagement.updateTargeting(projectKey, flagKey, envKey, rules);
    }
}
