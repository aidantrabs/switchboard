package com.switchboard.adapter.output.persistence.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "conditions")
public class ConditionJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "targeting_rule_id", nullable = false)
    private UUID targetingRuleId;

    @Column(nullable = false)
    private String attribute;

    @Column(nullable = false)
    private String operator;

    @Column(name = "value_json", nullable = false)
    private String valueJson;

    protected ConditionJpaEntity() {}

    public ConditionJpaEntity(UUID id, UUID targetingRuleId, String attribute,
                              String operator, String valueJson) {
        this.id = id;
        this.targetingRuleId = targetingRuleId;
        this.attribute = attribute;
        this.operator = operator;
        this.valueJson = valueJson;
    }

    public UUID getId() { return id; }
    public UUID getTargetingRuleId() { return targetingRuleId; }
    public String getAttribute() { return attribute; }
    public String getOperator() { return operator; }
    public String getValueJson() { return valueJson; }
}
