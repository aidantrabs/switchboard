CREATE TABLE targeting_rules (
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    flag_env_config_id UUID NOT NULL REFERENCES flag_environment_configs(id) ON DELETE CASCADE,
    priority           INT NOT NULL,
    served_variant_key VARCHAR(255) NOT NULL,
    created_at         TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE conditions (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    targeting_rule_id UUID NOT NULL REFERENCES targeting_rules(id) ON DELETE CASCADE,
    attribute         VARCHAR(255) NOT NULL,
    operator          VARCHAR(50) NOT NULL,
    value_json        TEXT NOT NULL
);
