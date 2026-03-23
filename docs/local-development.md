# local development

## docker compose

start all infrastructure:

```bash
docker compose -f docker/docker-compose.yml up
```

services:

| service | url |
|---------|-----|
| switchboard-server | http://localhost:8080 |
| switchboard-dashboard | http://localhost:5173 |
| demo service | http://localhost:8081/demo |
| postgresql | localhost:5432 |
| redis | localhost:6379 |
| kafka | localhost:9092 |

## running the server standalone

if you prefer running the server outside docker:

```bash
# start just the infrastructure
docker compose -f docker/docker-compose.yml up postgres redis kafka

# run the server
./gradlew :switchboard-server:bootRun
```

## running the dashboard in dev mode

```bash
cd switchboard-dashboard
npm install
npm run dev
```

the vite dev server proxies `/api` requests to `localhost:8080`.

## running the cli

```bash
./gradlew :switchboard-cli:run --args="flags list --api-url http://localhost:8080 --project demo-project"
```

## running tests

```bash
# all tests
./gradlew test

# server tests only
./gradlew :switchboard-server:test

# sdk tests only
./gradlew :switchboard-sdk-java:test
```

## seed data

the docker compose setup automatically loads `docker/seed-data.sql` which
creates:

- organization: switchboard-demo
- project: demo-project
- environments: dev, staging, production
- sample flags: new-checkout, dark-mode, premium-dashboard, search-algorithm
- flag configs with various rollout percentages
- audit log entries
