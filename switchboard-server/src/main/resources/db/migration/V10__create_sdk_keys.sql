CREATE TABLE sdk_keys (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id     UUID NOT NULL REFERENCES projects(id),
    environment_id UUID NOT NULL REFERENCES environments(id),
    key_hash       VARCHAR(255) NOT NULL UNIQUE,
    name           VARCHAR(255) NOT NULL,
    last_used_at   TIMESTAMP,
    created_at     TIMESTAMP NOT NULL DEFAULT now(),
    revoked_at     TIMESTAMP
);
