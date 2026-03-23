# local mode

the sdk supports a standalone local mode where no running switchboard server is
needed. flags are loaded from a json file.

## configuration

### spring boot starter

```yaml
switchboard:
  mode: local
  local-flags-file: classpath:flags.json
```

### pure java

```java
SwitchboardClient client = SwitchboardClient.builder()
    .localFlagsFile("classpath:flags.json")
    .build();
```

## flags file format

```json
{
  "flags": {
    "new-checkout": { "enabled": true, "variant": "on" },
    "dark-mode": { "enabled": false, "variant": "off" },
    "premium-dashboard": { "enabled": true, "variant": "on" }
  }
}
```

each flag needs:

- `enabled`: boolean — whether the flag is on
- `variant`: string — the active variant key

## use cases

- **local development** without running the control plane
- **unit and integration tests** with predictable flag values
- **ci/cd pipelines** with no external dependencies
- **offline or air-gapped environments**

## file locations

the `local-flags-file` value supports:

- `classpath:flags.json` — loaded from the classpath (checked into the repo)
- `/path/to/flags.json` — loaded from the filesystem

## example

see `examples/flags.json` for a complete example.
