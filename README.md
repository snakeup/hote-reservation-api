# Hotel Reservation API — Interview Exercise

## Stack

| | |
|---|---|
| Java | 25 |
| Spring Boot | 4.0.6 |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| Tests | JUnit 5 · Mockito · Testcontainers · AssertJ |

## Quick start

```bash
# 1. Start the database
docker compose up -d

# 2. Run the application
./mvnw spring-boot:run

# 3. Run all tests (requires Docker)
./mvnw test
```

API available at `http://localhost:8080`.

## Domain

```
Room        — hotel rooms (SINGLE, DOUBLE, SUITE, PENTHOUSE)
Guest       — registered guests
Reservation — links a Guest to a Room for a date range
Payment     — one payment per reservation
```

## What's already implemented

| Layer | What |
|---|---|
| Entities | `Room`, `Guest`, `Payment` fully annotated. `Reservation` needs your JPA annotations. |
| Repositories | All four repositories with custom queries. |
| Service | `RoomService` fully implemented — use it as a reference. |
| Controller | `RoomController` fully implemented — use it as a reference. |
| Exception handling | `GlobalExceptionHandler` wired up. |
| Tests | `RoomServiceTest` and `RoomControllerIntegrationTest` as working examples. |

## Your tasks — see TODO.md
