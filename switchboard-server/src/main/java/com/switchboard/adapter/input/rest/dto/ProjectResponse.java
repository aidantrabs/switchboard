package com.switchboard.adapter.input.rest.dto;

import com.switchboard.domain.model.Project;

import java.time.Instant;

public record ProjectResponse(String id, String name, String key, Instant createdAt) {

    public static ProjectResponse from(Project project) {
        return new ProjectResponse(
            project.getId().toString(), project.getName(),
            project.getKey(), project.getCreatedAt()
        );
    }
}
