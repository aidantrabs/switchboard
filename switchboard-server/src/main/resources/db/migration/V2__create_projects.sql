CREATE TABLE projects (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    organization_id UUID NOT NULL REFERENCES organizations(id),
    name            VARCHAR(255) NOT NULL,
    key             VARCHAR(255) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (organization_id, key)
);
