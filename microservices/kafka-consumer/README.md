# Kafka Consumer Service

Spring Boot worker service consuming query events from Kafka.

## Run locally (no Docker)
```
mvn spring-boot:run
```

The service listens to `query.created.v1`, processes each message, and updates
`query_request` status/result in MySQL.

## Run tests
```
mvn test
```

## Notes
- Consumes messages published by `microservices/api`
- Meant for async query demo flow used by the frontend
