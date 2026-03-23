package com.switchboard.infrastructure.config;

import com.switchboard.application.port.input.AuditLogQueryUseCase;
import com.switchboard.application.port.input.FlagEvaluationUseCase;
import com.switchboard.application.port.input.FlagManagementUseCase;
import com.switchboard.application.port.input.ProjectManagementUseCase;
import com.switchboard.application.port.input.StaleFlagUseCase;
import com.switchboard.application.port.output.AuditLogPersistencePort;
import com.switchboard.application.port.output.FlagChangeEventPort;
import com.switchboard.application.port.output.FlagPersistencePort;
import com.switchboard.application.port.output.ProjectPersistencePort;
import com.switchboard.application.service.AuditLogQueryService;
import com.switchboard.application.service.FlagEvaluationService;
import com.switchboard.application.service.FlagManagementService;
import com.switchboard.application.service.ProjectManagementService;
import com.switchboard.application.service.StaleFlagService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationServiceConfig {

    @Bean
    public FlagManagementUseCase flagManagementUseCase(FlagPersistencePort flagPersistence,
                                                       ProjectPersistencePort projectPersistence,
                                                       AuditLogPersistencePort auditLogPersistence,
                                                       FlagChangeEventPort flagChangeEvent) {
        return new FlagManagementService(flagPersistence, projectPersistence,
            auditLogPersistence, flagChangeEvent);
    }

    @Bean
    public FlagEvaluationUseCase flagEvaluationUseCase(FlagPersistencePort flagPersistence,
                                                       ProjectPersistencePort projectPersistence) {
        return new FlagEvaluationService(flagPersistence, projectPersistence);
    }

    @Bean
    public ProjectManagementUseCase projectManagementUseCase(ProjectPersistencePort projectPersistence) {
        return new ProjectManagementService(projectPersistence);
    }

    @Bean
    public AuditLogQueryUseCase auditLogQueryUseCase(AuditLogPersistencePort auditLogPersistence) {
        return new AuditLogQueryService(auditLogPersistence);
    }

    @Bean
    public StaleFlagUseCase staleFlagUseCase(FlagPersistencePort flagPersistence,
                                             ProjectPersistencePort projectPersistence) {
        return new StaleFlagService(flagPersistence, projectPersistence);
    }
}
