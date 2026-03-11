package com.switchboard.domain.exception;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(String projectKey) {
        super("project not found: " + projectKey);
    }
}
