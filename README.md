# Spring Boot starter

A minimal Spring Boot 3 app on Java 21, ready to deploy on [Containify](https://containify.cloud):

- Spring Web with `JdbcClient`, and Postgres through the PostgreSQL JDBC driver
- [Flyway](https://documentation.red-gate.com/flyway) migrations in `src/main/resources/db/migration`, applied at startup
- `GET /` answers JSON and counts visits in Postgres; Actuator's `/actuator/health` checks the database
- A layered, multi-stage Dockerfile: dependencies in their own layer, the Temurin 21 JRE, a non-root user

## Run it locally

You need Java 21 and a Postgres database. The defaults in `application.properties` point at
`localhost:5432` with the user and password `postgres`:

```bash
docker run -d --name starter-db -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:17
./mvnw spring-boot:run
curl localhost:8080
```

Or with Docker only:

```bash
docker build -t spring-boot-starter .
docker run --rm -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/postgres \
  spring-boot-starter
```

## Configuration

Spring Boot reads any property from an environment variable of the same name in upper case,
with `.` and `-` replaced by `_`.

| Variable | Default | |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/postgres` | A `jdbc:postgresql://host:5432/dbname` URL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | |
| `PORT` | `8080` | The port to listen on |
| `SPRING_FLYWAY_ENABLED` | `true` | `false` skips migrations at startup |

## Migrations

Add `V2__description.sql`, `V3__…` and so on next to the first migration. Flyway applies the new
ones when the app starts, under a lock in the database, so replicas starting together don't
race. To run them once per deploy instead, set `SPRING_FLYWAY_ENABLED=false` and use this as the
app's pre-deploy command:

```bash
java -jar application.jar --spring.flyway.enabled=true --spring.main.web-application-type=none
```

## Deploy on Containify

Deploy the [Spring Boot starter template](https://containify.cloud/templates/spring-boot): it
builds this repository with its Dockerfile and adds a Postgres database, with the three
`SPRING_DATASOURCE_` variables already set. To make it your own, fork this repository and point
the app's source at your fork.

See the [Spring Boot guide](https://containify.cloud/docs/guides/spring-boot) for the details.

## License

MIT
