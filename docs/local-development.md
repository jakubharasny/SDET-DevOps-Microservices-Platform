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

Note: the Compose setup expects a `Dockerfile` in `microservices/frontend` and `microservices/api`.

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
  - tests/e2e

API schema (OpenAPI):
- Generated during API tests from `/v3/api-docs`.
- Output file: `docs/openapi/api.json`

End-to-end (Playwright):
```
cd tests/e2e
npm install
npx playwright install
npm test
```

## Local MySQL for SQL practice
See `docs/local-database.md`.

## Git hooks (lint before push)
Hooks live inside each microservice and are triggered by a root pre-push hook.
They auto-format OpenAPI output, run fast local tests for each service, and lint Playwright tests.
Enable it once per clone:

```
git config core.hooksPath .githooks
```

OpenAPI formatting helpers live under `microservices/api/scripts`.
