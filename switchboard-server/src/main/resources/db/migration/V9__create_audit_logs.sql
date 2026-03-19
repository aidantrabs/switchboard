CREATE TABLE audit_logs (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id        UUID NOT NULL REFERENCES projects(id),
    flag_key          VARCHAR(255),
    environment_key   VARCHAR(100),
    action            VARCHAR(100) NOT NULL,
    changed_by        VARCHAR(255) NOT NULL,
    before_state_json TEXT,
    after_state_json  TEXT,
    timestamp         TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_audit_logs_project_timestamp ON audit_logs(project_id, timestamp DESC);
CREATE INDEX idx_audit_logs_flag_key ON audit_logs(flag_key);
