# AGENTS.md

## Cursor Cloud specific instructions

### Project overview

SDET/DevOps Microservices Platform monorepo. Two Spring Boot 3.2.5 services (Java 17 target, runs fine on Java 21):

- **API** (`microservices/api`, port 8081) — REST API serving `/api/ping` and `/api/countries`; has Swagger UI at `/swagger-ui.html`
- **Frontend** (`microservices/frontend`, port 8080) — Thymeleaf UI that calls the API to render EU country currencies

MySQL (port 3306) exists for SQL practice only; no service depends on it.

See `project-overview.md` for phase discipline and `.cursorrules` for override rules.

### Running services

Start API first (frontend calls it):

```
cd microservices/api && mvn spring-boot:run
cd microservices/frontend && mvn spring-boot:run
```

Or use Docker Compose (builds images, slower): `docker compose -f deploy/compose/docker-compose.yml up --build`

### Lint, test, build

Standard commands per service — see `README.md` for quick reference:

- **Lint:** `mvn spotless:check` (per service dir)
- **Test:** `mvn test` (per service dir; API has 4 tests, Frontend has 3)
- **Build:** `mvn compile` (per service dir)

### Non-obvious caveats

- No Maven wrapper (`mvnw`) in the repo — system `mvn` is required.
- Docker daemon needs manual start in cloud VMs: `sudo dockerd &>/tmp/dockerd.log &` then `sudo chmod 666 /var/run/docker.sock`.
- The frontend's `API_BASE_URL` defaults to `http://localhost:8081`. When running via Docker Compose, the compose file sets this explicitly.
- Git hooks: enable with `git config core.hooksPath .githooks` (runs spotless + tests on pre-push).
- API lint script (`microservices/api/scripts/lint.sh`) also checks OpenAPI JSON formatting via `python3`.
