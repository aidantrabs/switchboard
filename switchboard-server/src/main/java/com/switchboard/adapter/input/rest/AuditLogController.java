package com.switchboard.adapter.input.rest;

import com.switchboard.adapter.input.rest.dto.AuditLogResponse;
import com.switchboard.application.port.input.AuditLogQueryUseCase;
import com.switchboard.application.port.input.ProjectManagementUseCase;
import com.switchboard.domain.model.Project;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AuditLogController {

    private final AuditLogQueryUseCase auditLogQuery;
    private final ProjectManagementUseCase projectManagement;

    public AuditLogController(AuditLogQueryUseCase auditLogQuery,
                              ProjectManagementUseCase projectManagement) {
        this.auditLogQuery = auditLogQuery;
        this.projectManagement = projectManagement;
    }

    @GetMapping("/projects/{projectKey}/audit-log")
    public List<AuditLogResponse> getProjectAuditLog(
            @PathVariable String projectKey,
            @RequestParam(required = false) String flagKey,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to,
            @RequestParam(defaultValue = "50") int limit) {

        Project project = projectManagement.getProject(projectKey);

        if (flagKey != null) {
            return auditLogQuery.queryByFlag(project.getId(), flagKey, limit).stream()
                .map(AuditLogResponse::from)
                .toList();
        }

        Instant effectiveFrom = from != null ? from : Instant.now().minus(30, ChronoUnit.DAYS);
        Instant effectiveTo = to != null ? to : Instant.now();

        return auditLogQuery.queryByProject(project.getId(), effectiveFrom, effectiveTo).stream()
            .map(AuditLogResponse::from)
            .toList();
    }
}
