# Local MySQL (dev + SQL practice)

This database is for local development and SQL practice only. The services do not use it yet.

## Start MySQL

```
docker compose -f deploy/compose/docker-compose.yml up -d mysql
```

Connection details:
- Host: `127.0.0.1`
- Port: `3306`
- Database: `sdet_dev`
- User: `sdet`
- Password: `sdetpass`

## Apply schema

```
docker compose -f deploy/compose/docker-compose.yml exec -T mysql \
  mysql -u sdet -psdetpass sdet_dev < deploy/mysql/schema.sql
```

## Seed with fake data (Java)

```
DB_HOST=127.0.0.1 \
DB_PORT=3306 \
DB_NAME=sdet_dev \
DB_USER=sdet \
DB_PASSWORD=sdetpass \
SEED_COUNT=100 \
mvn -q -f deploy/mysql/pom.xml exec:java
```

## Reset and reseed

```
docker compose -f deploy/compose/docker-compose.yml exec -T mysql \
  mysql -u root -prootpass -e "DROP DATABASE IF EXISTS sdet_dev; CREATE DATABASE sdet_dev;"

docker compose -f deploy/compose/docker-compose.yml exec -T mysql \
  mysql -u sdet -psdetpass sdet_dev < deploy/mysql/schema.sql

DB_HOST=127.0.0.1 \
DB_PORT=3306 \
DB_NAME=sdet_dev \
DB_USER=sdet \
DB_PASSWORD=sdetpass \
SEED_COUNT=100 \
mvn -q -f deploy/mysql/pom.xml exec:java
```
