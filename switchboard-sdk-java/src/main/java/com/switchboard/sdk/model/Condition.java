package com.switchboard.sdk.model;

public class Condition {

    private String attribute;
    private String operator;
    private String value;

    public Condition() {}

    public Condition(String attribute, String operator, String value) {
        this.attribute = attribute;
        this.operator = operator;
        this.value = value;
    }

    public String getAttribute() { return attribute; }
    public String getOperator() { return operator; }
    public String getValue() { return value; }

    public void setAttribute(String attribute) { this.attribute = attribute; }
    public void setOperator(String operator) { this.operator = operator; }
    public void setValue(String value) { this.value = value; }
}
