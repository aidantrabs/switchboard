package com.switchboard.adapter.input.rest.dto;

import com.switchboard.domain.model.Environment;

public record EnvironmentResponse(String id, String name, String key, int sortOrder) {

    public static EnvironmentResponse from(Environment env) {
        return new EnvironmentResponse(
            env.getId().toString(), env.getName(), env.getKey(), env.getSortOrder()
        );
    }
}
