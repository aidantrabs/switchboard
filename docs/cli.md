# cli reference

## configuration

create `~/.switchboard/config.yml`:

```yaml
api-url: http://localhost:8080
api-key: sb_demo_key
default-project: demo-project
default-environment: dev
output: table
```

all values can be overridden with command-line flags.

## global options

| flag | description |
|------|-------------|
| `--api-url <url>` | switchboard api url |
| `--api-key <key>` | api key |
| `--project <key>` | default project key |
| `--config <path>` | config file path |
| `--output json\|table` | output format (default: table) |

## commands

### flags list

```bash
switchboard flags list --project demo-project
```

### flags get

```bash
switchboard flags get new-checkout --project demo-project
```

### flags create

```bash
switchboard flags create --key my-flag --type release --project demo-project
```

### flags toggle

```bash
switchboard flags toggle new-checkout --env dev --project demo-project
```

### flags rollout

```bash
switchboard flags rollout new-checkout --percentage 50 --env staging --project demo-project
```

### flags delete

```bash
switchboard flags delete old-flag --project demo-project
```

### evaluate

```bash
switchboard evaluate new-checkout --user user-123 --env dev --project demo-project
```

### audit

```bash
switchboard audit --project demo-project --last 10
switchboard audit --project demo-project --flag new-checkout
```

### projects list

```bash
switchboard projects list
```

### envs list

```bash
switchboard envs list --project demo-project
```

## json output

pipe to jq for scripting:

```bash
switchboard flags list --output json | jq '.[] | .key'
```
