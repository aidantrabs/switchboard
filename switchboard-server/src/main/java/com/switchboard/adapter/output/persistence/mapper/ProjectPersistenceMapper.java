package com.switchboard.adapter.output.persistence.mapper;

import com.switchboard.adapter.output.persistence.entity.EnvironmentJpaEntity;
import com.switchboard.adapter.output.persistence.entity.ProjectJpaEntity;
import com.switchboard.domain.model.Environment;
import com.switchboard.domain.model.Project;

public final class ProjectPersistenceMapper {

    private ProjectPersistenceMapper() {}

    public static ProjectJpaEntity toJpaEntity(Project project) {
        return new ProjectJpaEntity(
            project.getId(), project.getOrganizationId(),
            project.getName(), project.getKey(), project.getCreatedAt()
        );
    }

    public static Project toDomain(ProjectJpaEntity entity) {
        return new Project(
            entity.getId(), entity.getOrganizationId(),
            entity.getName(), entity.getKey(), entity.getCreatedAt()
        );
    }

    public static Environment toDomain(EnvironmentJpaEntity entity) {
        return new Environment(
            entity.getId(), entity.getProjectId(),
            entity.getName(), entity.getKey(), entity.getSortOrder()
        );
    }
}
