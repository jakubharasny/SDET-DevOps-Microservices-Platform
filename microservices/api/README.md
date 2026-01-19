# API Service

Spring Boot REST API service.

## Run locally (no Docker)
```
mvn spring-boot:run
```

Then open `http://localhost:8081/api/countries`.

## Run tests
```
mvn test
```

## Schema
OpenAPI schema is generated during tests and written to `docs/openapi/api.json`.

## Notes
- Component tests live under `src/test/java/com/example/api/component`
- Unit tests live under `src/test/java/com/example/api/unit`
