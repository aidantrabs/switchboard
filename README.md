# switchboard

an enterprise feature flag control plane built with spring boot, following
hexagonal architecture with event-driven updates.

## quick start

```bash
git clone https://github.com/aidantrabs/switchboard.git
cd switchboard
docker compose -f docker/docker-compose.yml up
```

once running:

- **dashboard**: http://localhost:5173
- **api**: http://localhost:8080
- **demo service**: http://localhost:8081/demo

## modules

| module | description |
|--------|-------------|
| `switchboard-server` | spring boot control plane |
| `switchboard-sdk-java` | pure java sdk (zero spring deps) |
| `switchboard-spring-boot-starter` | auto-config wrapper for sdk |
| `switchboard-openfeature-provider` | openfeature bridge |
| `switchboard-cli` | picocli terminal tool |
| `switchboard-dashboard` | react spa (vite + tanstack) |
| `switchboard-demo-service` | sample app using the starter |

## sdk integration

add the starter dependency:

```kotlin
dependencies {
    implementation("com.switchboard:switchboard-spring-boot-starter:0.1.0-SNAPSHOT")
}
```

configure in `application.yml`:

```yaml
switchboard:
  api-url: https://switchboard.internal.com
  api-key: ${SWITCHBOARD_API_KEY}
  project: my-service
  environment: production
```

use in code:

```java
@Service
public class MyService {

    private final SwitchboardClient switchboard;

    public MyService(SwitchboardClient switchboard) {
        this.switchboard = switchboard;
    }

    public void doSomething(User user) {
        var context = EvaluationContext.builder()
            .userId(user.getId())
            .attribute("plan", user.getPlan())
            .build();

        if (switchboard.isEnabled("new-feature", context)) {
            // new path
        }
    }
}
```

## local mode

for development and testing without a running server:

```yaml
switchboard:
  mode: local
  local-flags-file: classpath:flags.json
```

```json
{
  "flags": {
    "new-feature": { "enabled": true, "variant": "on" },
    "dark-mode": { "enabled": false, "variant": "off" }
  }
}
```

## tech stack

**backend**: java 22, spring boot 3.4.3, postgresql, redis, kafka, flyway,
junit 5 + archunit, gradle 8.12

**frontend**: react 19, typescript, vite, tanstack (router + query + table),
tailwind css

## architecture

hexagonal architecture (ports and adapters). domain layer is pure java with
zero framework imports. see [docs/architecture.md](docs/architecture.md) for details.

## documentation

- [getting started](docs/getting-started.md)
- [architecture](docs/architecture.md)
- [cli reference](docs/cli.md)
- [local development](docs/local-development.md)
- [local mode](docs/local-mode.md)
