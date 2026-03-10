package com.switchboard.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Organization {

    private final UUID id;
    private final String name;
    private final String slug;
    private final Instant createdAt;

    public Organization(UUID id, String name, String slug, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.createdAt = createdAt;
    }

    public static Organization create(String name, String slug) {
        return new Organization(UUID.randomUUID(), name, slug, Instant.now());
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
