package com.switchboard.adapter.input.rest.dto;

import java.util.UUID;

public record CreateProjectRequest(UUID organizationId, String name, String key) {}
