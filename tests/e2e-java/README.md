# Playwright UI Flow Tests (Java)

Lightweight browser tests for the frontend UI flow.
The suite mocks `/api/countries` and `/api/queries*` calls in-browser so CI stays
fast and deterministic while still validating real user interactions.

## Install Playwright browsers

```
mvn -q -f tests/e2e-java/pom.xml exec:java -Dexec.args="install"
```

## Run the tests

```
mvn -q -f tests/e2e-java/pom.xml test
```

## Custom base URL

```
E2E_BASE_URL=http://localhost:8080 \
mvn -q -f tests/e2e-java/pom.xml test
```
