.PHONY: help dev server dashboard demo setup seed test test-unit test-arch clean db db-stop

help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-16s\033[0m %s\n", $$1, $$2}'

dev: db server

server:
	./gradlew :switchboard-server:bootRun --args='--spring.profiles.active=dev'

dashboard:
	cd switchboard-dashboard && npm run dev

demo:
	./gradlew :switchboard-demo-service:bootRun

setup: seed

seed:
	./scripts/setup.sh

db:
	@docker start switchboard-pg 2>/dev/null || \
		docker run -d --name switchboard-pg \
			-e POSTGRES_DB=switchboard \
			-e POSTGRES_USER=switchboard \
			-e POSTGRES_PASSWORD=switchboard \
			-p 5432:5432 \
			postgres:16-alpine
	@echo "postgres running on localhost:5432"

db-stop:
	docker stop switchboard-pg

stack:
	docker compose -f docker/docker-compose.yml up

stack-down:
	docker compose -f docker/docker-compose.yml down

test:
	./gradlew :switchboard-server:test \
		--tests "com.switchboard.domain.*" \
		--tests "com.switchboard.application.*" \
		--tests "com.switchboard.architecture.*"
	./gradlew :switchboard-sdk-java:test
	./gradlew :switchboard-openfeature-provider:test

test-unit:
	./gradlew :switchboard-server:test \
		--tests "com.switchboard.domain.*" \
		--tests "com.switchboard.application.*"
	./gradlew :switchboard-sdk-java:test

test-arch:
	./gradlew :switchboard-server:test --tests "com.switchboard.architecture.*"

test-integration:
	./gradlew :switchboard-server:test --tests "com.switchboard.integration.*"

test-dashboard:
	cd switchboard-dashboard && npx tsc --noEmit && npx vite build

build:
	./gradlew compileJava

build-server:
	./gradlew :switchboard-server:bootJar

build-dashboard:
	cd switchboard-dashboard && npm ci && npx vite build

swagger:
	open http://localhost:8080/swagger-ui.html

cli:
	./gradlew :switchboard-cli:run --args="$(ARGS) --api-url http://localhost:8080"

clean:
	./gradlew clean
	rm -rf switchboard-dashboard/dist switchboard-dashboard/node_modules
