package com.switchboard.adapter.input.rest;

import com.switchboard.adapter.input.rest.dto.FlagResponse;
import com.switchboard.application.port.input.StaleFlagUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectKey}/flags")
public class StaleFlagController {

    private final StaleFlagUseCase staleFlagUseCase;

    public StaleFlagController(StaleFlagUseCase staleFlagUseCase) {
        this.staleFlagUseCase = staleFlagUseCase;
    }

    @GetMapping("/stale")
    public List<FlagResponse> getStaleFlags(@PathVariable String projectKey) {
        return staleFlagUseCase.findStaleFlags(projectKey).stream()
            .map(FlagResponse::from)
            .toList();
    }
}
