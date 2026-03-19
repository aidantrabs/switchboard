CREATE TABLE variants (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    flag_id    UUID NOT NULL REFERENCES feature_flags(id) ON DELETE CASCADE,
    key        VARCHAR(255) NOT NULL,
    value_json TEXT NOT NULL,
    UNIQUE (flag_id, key)
);
