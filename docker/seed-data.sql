-- organization
INSERT INTO organizations (id, name, slug) VALUES
    ('00000000-0000-0000-0000-000000000001', 'Switchboard Demo', 'switchboard-demo');

-- project
INSERT INTO projects (id, organization_id, name, key) VALUES
    ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000001', 'Demo Project', 'demo-project');

-- environments
INSERT INTO environments (id, project_id, name, key, sort_order) VALUES
    ('00000000-0000-0000-0000-000000000010', '00000000-0000-0000-0000-000000000002', 'Development', 'dev', 0),
    ('00000000-0000-0000-0000-000000000011', '00000000-0000-0000-0000-000000000002', 'Staging', 'staging', 1),
    ('00000000-0000-0000-0000-000000000012', '00000000-0000-0000-0000-000000000002', 'Production', 'production', 2);

-- feature flags
INSERT INTO feature_flags (id, project_id, key, name, description, flag_type, default_variant) VALUES
    ('00000000-0000-0000-0000-000000000020', '00000000-0000-0000-0000-000000000002', 'new-checkout', 'New Checkout Flow', 'Redesigned checkout experience', 'RELEASE', 'off'),
    ('00000000-0000-0000-0000-000000000021', '00000000-0000-0000-0000-000000000002', 'dark-mode', 'Dark Mode', 'Enable dark mode UI', 'RELEASE', 'off'),
    ('00000000-0000-0000-0000-000000000022', '00000000-0000-0000-0000-000000000002', 'premium-dashboard', 'Premium Dashboard', 'Premium-only dashboard features', 'PERMISSION', 'off'),
    ('00000000-0000-0000-0000-000000000023', '00000000-0000-0000-0000-000000000002', 'search-algorithm', 'Search Algorithm', 'A/B test search ranking', 'EXPERIMENT', 'control');

-- variants
INSERT INTO variants (id, flag_id, key, value_json) VALUES
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000020', 'on', 'true'),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000020', 'off', 'false'),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000021', 'on', 'true'),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000021', 'off', 'false'),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000022', 'on', 'true'),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000022', 'off', 'false'),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000023', 'control', '{"algorithm":"v1"}'),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000023', 'treatment', '{"algorithm":"v2"}');

-- flag environment configs
INSERT INTO flag_environment_configs (id, flag_id, environment_id, enabled, rollout_percentage) VALUES
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000020', '00000000-0000-0000-0000-000000000010', true, 100),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000020', '00000000-0000-0000-0000-000000000011', true, 50),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000020', '00000000-0000-0000-0000-000000000012', false, 0),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000021', '00000000-0000-0000-0000-000000000010', true, 100),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000021', '00000000-0000-0000-0000-000000000012', false, 0),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000022', '00000000-0000-0000-0000-000000000010', true, 100),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000023', '00000000-0000-0000-0000-000000000010', true, 50);

-- sdk key (plaintext: sb_demo_key, stored as sha256 hash)
INSERT INTO sdk_keys (id, project_id, environment_id, key_hash, name) VALUES
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000010',
     'a]Demo SDK Key');

-- audit log entries
INSERT INTO audit_logs (id, project_id, flag_key, environment_key, action, changed_by, timestamp) VALUES
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000002', 'new-checkout', null, 'FLAG_CREATED', 'seed', now() - interval '7 days'),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000002', 'new-checkout', 'dev', 'FLAG_TOGGLED', 'seed', now() - interval '6 days'),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000002', 'dark-mode', null, 'FLAG_CREATED', 'seed', now() - interval '5 days'),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000002', 'premium-dashboard', null, 'FLAG_CREATED', 'seed', now() - interval '4 days'),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000002', 'search-algorithm', null, 'FLAG_CREATED', 'seed', now() - interval '3 days'),
    (gen_random_uuid(), '00000000-0000-0000-0000-000000000002', 'new-checkout', 'staging', 'ROLLOUT_UPDATED', 'seed', now() - interval '2 days');
