# SDET / DevOps Microservices Platform

Monorepo for a small microservices system plus a realistic DevOps/SDET delivery platform.

## Services
- `microservices/frontend` — Spring Boot + Thymeleaf UI
- `microservices/api` — Spring Boot REST API

## Quick start
```
docker compose -f deploy/compose/docker-compose.yml up --build
```

Endpoints:
- Frontend UI: `http://localhost:8080/`
- Frontend health: `http://localhost:8080/actuator/health`
- API ping: `http://localhost:8081/api/ping`
- API countries: `http://localhost:8081/api/countries`

## Testing
Run tests per service:
```
cd microservices/api && mvn test
cd microservices/frontend && mvn test
```

## Docs
- `docs/local-development.md`
