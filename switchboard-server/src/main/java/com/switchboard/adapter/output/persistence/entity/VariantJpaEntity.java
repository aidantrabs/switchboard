package com.switchboard.adapter.output.persistence.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "variants", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"flag_id", "key"})
})
public class VariantJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "flag_id", nullable = false)
    private UUID flagId;

    @Column(nullable = false)
    private String key;

    @Column(name = "value_json", nullable = false)
    private String valueJson;

    protected VariantJpaEntity() {}

    public VariantJpaEntity(UUID id, UUID flagId, String key, String valueJson) {
        this.id = id;
        this.flagId = flagId;
        this.key = key;
        this.valueJson = valueJson;
    }

    public UUID getId() { return id; }
    public UUID getFlagId() { return flagId; }
    public String getKey() { return key; }
    public String getValueJson() { return valueJson; }
}
