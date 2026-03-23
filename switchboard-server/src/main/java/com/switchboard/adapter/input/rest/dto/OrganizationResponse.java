package com.switchboard.adapter.input.rest.dto;

import com.switchboard.domain.model.Organization;

import java.time.Instant;

public record OrganizationResponse(String id, String name, String slug, Instant createdAt) {

    public static OrganizationResponse from(Organization org) {
        return new OrganizationResponse(
            org.getId().toString(), org.getName(), org.getSlug(), org.getCreatedAt());
    }
}
