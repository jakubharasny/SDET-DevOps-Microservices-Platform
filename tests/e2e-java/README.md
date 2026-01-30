# Playwright E2E (Java)

Lightweight browser tests for the local UI. Requires frontend + API running.

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
