# getting started

## prerequisites

- java 22+
- docker and docker compose (for infrastructure)
- node 22+ (for dashboard development)

## running locally

the fastest way to get everything running:

```bash
git clone https://github.com/aidantrabs/switchboard.git
cd switchboard
docker compose -f docker/docker-compose.yml up
```

this starts: postgresql, redis, kafka, switchboard-server, dashboard, and a demo
service. seed data is loaded automatically.

## sdk integration

### spring boot

add the starter to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.switchboard:switchboard-spring-boot-starter:0.1.0-SNAPSHOT")
}
```

configure in `application.yml`:

```yaml
switchboard:
  api-url: http://localhost:8080
  api-key: sb_demo_key
  project: demo-project
  environment: dev
```

inject and use:

```java
@Service
public class MyService {

    private final SwitchboardClient switchboard;

    public MyService(SwitchboardClient switchboard) {
        this.switchboard = switchboard;
    }

    public void handle(String userId) {
        var ctx = EvaluationContext.builder().userId(userId).build();

        if (switchboard.isEnabled("new-feature", ctx)) {
            // feature is on for this user
        }
    }
}
```

### pure java (no spring)

```java
SwitchboardClient client = SwitchboardClient.builder()
    .apiUrl("http://localhost:8080")
    .apiKey("sb_demo_key")
    .project("demo-project")
    .environment("dev")
    .build();

var ctx = EvaluationContext.builder().userId("user-123").build();
boolean enabled = client.isEnabled("new-feature", ctx);
```

### openfeature

```java
OpenFeatureAPI.getInstance().setProvider(
    new SwitchboardProvider(switchboardClient)
);

Client client = OpenFeatureAPI.getInstance().getClient();
boolean enabled = client.getBooleanValue("new-feature", false, ctx);
```

## publishing sdk locally

```bash
./gradlew :switchboard-sdk-java:publishToMavenLocal
./gradlew :switchboard-spring-boot-starter:publishToMavenLocal
```
