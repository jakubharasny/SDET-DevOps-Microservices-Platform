# Local development

## Prerequisites

- Java 17+
- Maven
- Docker (for Compose runs)

## Run the frontend locally (no Docker)

From the repo root:

```
cd microservices/frontend
mvn spring-boot:run
```

Then open `http://localhost:8080`.

## Run via Docker Compose (Phase 1 target)

From the repo root:

```
docker compose -f deploy/compose/docker-compose.yml up --build
```

Endpoints:
- Frontend UI: `http://localhost:8080/`
- Frontend health: `http://localhost:8080/actuator/health`
- API ping: `http://localhost:8081/api/ping`
- API countries: `http://localhost:8081/api/countries`
- Async create query: `POST http://localhost:8081/api/queries`
- Async query status: `GET http://localhost:8081/api/queries/{id}`

Note: the Compose setup expects a `Dockerfile` in `microservices/frontend`, `microservices/api`, and `microservices/kafka-consumer`.

Testing layout (Phase 1):
- Unit tests live under each service:
  - microservices/api/src/test/java/com/example/api/unit
  - microservices/frontend/src/test/java/com/example/frontend/unit
- Component tests live under each service and are split by style:
  - MockMvc: microservices/api/src/test/java/com/example/api/component/mockmvc
  - Rest Assured (service-level): microservices/api/src/test/java/com/example/api/component/restassured
  - MockMvc: microservices/frontend/src/test/java/com/example/frontend/component/mockmvc
  - HTTP/TestRestTemplate: microservices/frontend/src/test/java/com/example/frontend/component/http
- End-to-end tests live under:
  - tests/e2e-java

API schema (OpenAPI):
- Generated during API tests from `/v3/api-docs`.
- Output file: `docs/openapi/api.json`

End-to-end (Playwright Java):
```
mvn -q -f tests/e2e-java/pom.xml exec:java \
  -Dexec.mainClass=com.microsoft.playwright.CLI \
  -Dexec.args="install"

mvn -q -f tests/e2e-java/pom.xml test
```

Playwright is split into two practical lanes:
- `UiMockedFlowTest` for UI microservice-level CI checks (browser-level mocks for
  `/api/countries` and `/api/queries*`).
- `CountriesE2ETest` for real full-stack validation against Compose (no mocks).

## Local MySQL for SQL practice
See `docs/local-database.md`.

## Git hooks (lint before push)
Hooks live inside each microservice and are triggered by a root pre-push hook
for `api`, `frontend`, and `kafka-consumer`.
They run Spotless checks and tests for each service. If Spotless fails, the hook
auto-applies formatting and stops the push so you can commit the formatting
changes before retrying.
`mvn test` in each service includes your current unit/component/integration tests
under `src/test/java` (all `*Test` classes).
Playwright E2E is intentionally separate from pre-push and should be run as an
explicit step.
Enable it once per clone:

```
git config core.hooksPath .githooks
```

OpenAPI formatting helpers live under `microservices/api/scripts`.
