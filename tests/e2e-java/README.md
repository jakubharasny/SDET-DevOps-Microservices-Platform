# Playwright E2E/UI Tests (Java)

This module contains platform-level Playwright only:
- `CountriesE2ETest`: real end-to-end flow (no mocks) against full Compose stack.

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
mvn -q -f tests/e2e-java/pom.xml -Dtest=CountriesE2ETest test
```

## Custom base URL

```
E2E_BASE_URL=http://localhost:8080 \
mvn -q -f tests/e2e-java/pom.xml test
```
