CREATE TABLE flag_environment_configs (
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    flag_id            UUID NOT NULL REFERENCES feature_flags(id) ON DELETE CASCADE,
    environment_id     UUID NOT NULL REFERENCES environments(id),
    enabled            BOOLEAN NOT NULL DEFAULT false,
    rollout_percentage INT NOT NULL DEFAULT 0 CHECK (rollout_percentage BETWEEN 0 AND 100),
    updated_at         TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (flag_id, environment_id)
);
