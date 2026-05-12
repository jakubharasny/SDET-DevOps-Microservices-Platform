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

Structure:
- `config/` - env-driven Playwright config (`E2E_BASE_URL`, headless, timeouts, run flag)
- `core/` - shared browser lifecycle base test
- `pages/` - page object model
- `support/` - reusable mock route setup
- `tests/` - scenario-focused test classes

Install Playwright browsers once:
```
mvn -q exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

Run only Playwright mocked UI tests:
```
RUN_PLAYWRIGHT_UI=true mvn -q \
  -Dtest=MockedCountriesFlowPlaywrightTest,MockedAsyncFlowPlaywrightTest test
```

Optional config:
- `E2E_BASE_URL=http://localhost:8080`
- `E2E_HEADLESS=true|false` (default: `true`)
- `E2E_ASSERT_TIMEOUT_MS=<ms>` (default: `10000`)

## Notes
- Component tests live under `src/test/java/com/example/frontend/component`
- Unit tests live under `src/test/java/com/example/frontend/unit`
