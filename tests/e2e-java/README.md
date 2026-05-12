# Playwright E2E/UI Tests (Java)

This module now has two Playwright layers:
- `UiMockedFlowTest`: UI microservice-level tests with browser-level mocks for
  `/api/countries` and `/api/queries*` (fast CI gate).
- `CountriesE2ETest`: real end-to-end flow (no mocks) against full Compose stack.

## Install Playwright browsers

```
mvn -q -f tests/e2e-java/pom.xml exec:java -Dexec.args="install"
```

## Run the tests

```
mvn -q -f tests/e2e-java/pom.xml test
```

## Run only mocked UI tests

```
mvn -q -f tests/e2e-java/pom.xml -Dtest=UiMockedFlowTest test
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
