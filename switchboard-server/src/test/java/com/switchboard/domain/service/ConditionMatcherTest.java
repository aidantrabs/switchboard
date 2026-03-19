package com.switchboard.domain.service;

import com.switchboard.domain.model.Condition;
import com.switchboard.domain.model.EvaluationContext;
import com.switchboard.domain.model.Operator;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConditionMatcherTest {

    @Test
    void equalsMatchesExactValue() {
        var condition = new Condition("user.country", Operator.EQUALS, "us");
        var context = new EvaluationContext("user-1", Map.of("user.country", "us"));

        assertTrue(ConditionMatcher.matches(condition, context));
    }

    @Test
    void equalsRejectsDifferentValue() {
        var condition = new Condition("user.country", Operator.EQUALS, "us");
        var context = new EvaluationContext("user-1", Map.of("user.country", "ca"));

        assertFalse(ConditionMatcher.matches(condition, context));
    }

    @Test
    void notEqualsRejectsMatchingValue() {
        var condition = new Condition("user.country", Operator.NOT_EQUALS, "us");
        var context = new EvaluationContext("user-1", Map.of("user.country", "us"));

        assertFalse(ConditionMatcher.matches(condition, context));
    }

    @Test
    void containsMatchesSubstring() {
        var condition = new Condition("user.email", Operator.CONTAINS, "@internal.com");
        var context = new EvaluationContext("user-1", Map.of("user.email", "dev@internal.com"));

        assertTrue(ConditionMatcher.matches(condition, context));
    }

    @Test
    void startsWithMatchesPrefix() {
        var condition = new Condition("user.email", Operator.STARTS_WITH, "admin");
        var context = new EvaluationContext("user-1", Map.of("user.email", "admin@example.com"));

        assertTrue(ConditionMatcher.matches(condition, context));
    }

    @Test
    void inListMatchesWhenValuePresent() {
        var condition = new Condition("user.plan", Operator.IN_LIST, "[\"premium\", \"enterprise\"]");
        var context = new EvaluationContext("user-1", Map.of("user.plan", "premium"));

        assertTrue(ConditionMatcher.matches(condition, context));
    }

    @Test
    void inListRejectsWhenValueAbsent() {
        var condition = new Condition("user.plan", Operator.IN_LIST, "[\"premium\", \"enterprise\"]");
        var context = new EvaluationContext("user-1", Map.of("user.plan", "free"));

        assertFalse(ConditionMatcher.matches(condition, context));
    }

    @Test
    void notInListMatchesWhenValueAbsent() {
        var condition = new Condition("user.plan", Operator.NOT_IN_LIST, "[\"premium\", \"enterprise\"]");
        var context = new EvaluationContext("user-1", Map.of("user.plan", "free"));

        assertTrue(ConditionMatcher.matches(condition, context));
    }

    @Test
    void greaterThanComparesNumerically() {
        var condition = new Condition("user.age", Operator.GREATER_THAN, "18");
        var context = new EvaluationContext("user-1", Map.of("user.age", "25"));

        assertTrue(ConditionMatcher.matches(condition, context));
    }

    @Test
    void lessThanComparesNumerically() {
        var condition = new Condition("user.age", Operator.LESS_THAN, "18");
        var context = new EvaluationContext("user-1", Map.of("user.age", "12"));

        assertTrue(ConditionMatcher.matches(condition, context));
    }

    @Test
    void semverGtComparesVersions() {
        var condition = new Condition("app.version", Operator.SEMVER_GT, "1.2.0");
        var context = new EvaluationContext("user-1", Map.of("app.version", "1.3.0"));

        assertTrue(ConditionMatcher.matches(condition, context));
    }

    @Test
    void semverLtComparesVersions() {
        var condition = new Condition("app.version", Operator.SEMVER_LT, "2.0.0");
        var context = new EvaluationContext("user-1", Map.of("app.version", "1.9.5"));

        assertTrue(ConditionMatcher.matches(condition, context));
    }

    @Test
    void missingAttributeReturnsFalse() {
        var condition = new Condition("user.country", Operator.EQUALS, "us");
        var context = new EvaluationContext("user-1", Map.of());

        assertFalse(ConditionMatcher.matches(condition, context));
    }
}
