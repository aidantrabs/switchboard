CREATE TABLE user_segments (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id UUID NOT NULL REFERENCES projects(id),
    name       VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE segment_conditions (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    segment_id UUID NOT NULL REFERENCES user_segments(id) ON DELETE CASCADE,
    attribute  VARCHAR(255) NOT NULL,
    operator   VARCHAR(50) NOT NULL,
    value_json TEXT NOT NULL
);
