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

If the schema already exists and you only want the new table:

```
docker compose -f deploy/compose/docker-compose.yml exec -T mysql \
  mysql -u sdet -psdetpass sdet_dev -e "
CREATE TABLE IF NOT EXISTS UserCredit (
  Id bigint NOT NULL AUTO_INCREMENT,
  UserRoleId bigint NOT NULL,
  Name varchar(100) NOT NULL,
  CreditLimit decimal(12, 2) NOT NULL,
  CurrencyCode char(3) NOT NULL,
  CardProvider varchar(50) NOT NULL,
  Created date NOT NULL,
  CONSTRAINT PK_UserCredit PRIMARY KEY (Id ASC),
  CONSTRAINT FK_UserCredit_UserRole FOREIGN KEY (UserRoleId) REFERENCES UserRole (Id)
);
"
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

## Example join query

```
SELECT
  ur.Name,
  uc.CreditLimit,
  uc.CurrencyCode,
  uc.CardProvider
FROM UserRole ur
JOIN UserCredit uc ON uc.UserRoleId = ur.Id
WHERE ur.IsEnabled = 1
ORDER BY uc.CreditLimit DESC;
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
