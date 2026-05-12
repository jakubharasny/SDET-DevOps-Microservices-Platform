# Playwright E2E/UI Tests (Java)

This module contains platform-level Playwright only:
- real end-to-end flow tests (no mocks) against full Compose stack
- organized as:
  - `config/` - environment-driven test configuration (`E2E_BASE_URL`, headless, timeouts)
  - `core/` - base test lifecycle for browser/context/page
  - `pages/` - page object model for UI actions/selectors
  - `tests/` - scenario-focused test classes

UI-mocked Playwright tests are owned by the frontend microservice under:
- `microservices/frontend/src/test/java/com/example/frontend/component/playwright`

## Install Playwright browsers

```
mvn -q -f tests/e2e-java/pom.xml exec:java -Dexec.args="install"
```

## Run the tests

```
mvn -q -f tests/e2e-java/pom.xml test
```

## Run only real end-to-end tests

Requires the full Compose stack (`frontend`, `api`, `kafka`, `kafka-consumer`, `mysql`) running.

```
mvn -q -f tests/e2e-java/pom.xml -Dtest=RealCountriesFlowE2ETest,RealAsyncKafkaFlowE2ETest test
```

## Custom base URL

```
E2E_BASE_URL=http://localhost:8080 \
mvn -q -f tests/e2e-java/pom.xml test
```

Optional config:
- `E2E_HEADLESS=true|false` (default: `true`)
- `E2E_ASSERT_TIMEOUT_MS=<ms>` (default: `10000`)
