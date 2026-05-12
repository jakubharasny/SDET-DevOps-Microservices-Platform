# Frontend Service

Spring Boot + Thymeleaf UI service.

## Run locally (no Docker)
```
mvn spring-boot:run
```

Then open `http://localhost:8080/`.

## Run tests
```
mvn test
```

## Playwright UI mocked flows (microservice-level)

Install Playwright browsers once:
```
mvn -q exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

Run only Playwright mocked UI tests:
```
mvn -q -Dtest=UiMockedFlowPlaywrightTest test
```

## Notes
- Component tests live under `src/test/java/com/example/frontend/component`
- Unit tests live under `src/test/java/com/example/frontend/unit`
