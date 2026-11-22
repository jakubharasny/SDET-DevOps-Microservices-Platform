# SDET / DevOps Microservices Platform

Portfolio monorepo: small microservices system + realistic DevOps/SDET delivery platform around it.

**Current status:** Phase 1 complete (local Compose + Kafka async flow); Phase 2 core CI live (path-filtered GitHub Actions + GHCR publish per service). Next: Sonar quality gate & Phase 3 (local Kubernetes).

Repo: https://github.com/jakubharasny/SDET-DevOps-Microservices-Platform

## Services
- `microservices/frontend` — Spring Boot + Thymeleaf UI
- `microservices/api` — Spring Boot REST API
- `microservices/kafka-consumer` — Spring Boot Kafka worker

## Quick start
```bash
# If a local MySQL already owns 3306:
#   brew services stop mysql
#   # or: MYSQL_HOST_PORT=3307 docker compose -f deploy/compose/docker-compose.yml up --build
docker compose -f deploy/compose/docker-compose.yml up --build
```

Endpoints:
- Frontend UI: `http://localhost:8080/`
- Frontend health: `http://localhost:8080/actuator/health`
- API ping: `http://localhost:8081/api/ping`
- API countries: `http://localhost:8081/api/countries`
- Async create query: `POST http://localhost:8081/api/queries`
- Async query status: `GET http://localhost:8081/api/queries/{id}`

Smoke the whole Phase 1 stack (health + countries + Kafka async):
```bash
./scripts/phase1-smoke.sh
```

## Testing
```bash
cd microservices/api && mvn test
cd microservices/frontend && mvn test
cd microservices/kafka-consumer && mvn test
```

Services target **Java 17** (Dockerfiles use Temurin 17). Local JDK 21+ usually works for `mvn test`; prefer 17 for CI parity.

End-to-end (Playwright Java, requires Compose stack running):
```bash
mvn -q -f tests/e2e-java/pom.xml exec:java \
  -Dexec.mainClass=com.microsoft.playwright.CLI \
  -Dexec.args="install"

mvn -q -f tests/e2e-java/pom.xml test
```

## Docs
- `project-overview.md` — architecture, phases, source of truth
- `docs/local-development.md` — local run + test layout
- `docs/local-database.md` — MySQL practice DB
