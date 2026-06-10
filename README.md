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

## Business Domain

This is a **hotel room reservation system**. Guests browse available rooms and book them for a stay. Staff manage the lifecycle from the initial booking through check-out.

### Entities

**Room** — A bookable hotel room. Has a type (`SINGLE`, `DOUBLE`, `SUITE`, `PENTHOUSE`), a floor, a capacity, and a fixed `pricePerNight`. Rooms can be filtered by type and queried for availability over a date range.

**Guest** — A registered person with a name, a unique email address, and a phone number. A guest can hold multiple reservations across different time periods.

**Reservation** — The core business object. Links one Guest to one Room for a stay from `checkInDate` to `checkOutDate`. The guest checks *out* on `checkOutDate` — they do not sleep there that night. `totalPrice` is calculated at booking time (`pricePerNight × nights`) and is never recalculated afterwards. Business rule: `checkOutDate` must be strictly after `checkInDate`.

**Payment** — One payment per reservation, enforced at the database level. Created at the same time as the reservation. If the reservation is deleted, the payment is deleted with it.

### Status lifecycles

```
Reservation:  PENDING ──► CONFIRMED ──► CHECKED_IN ──► CHECKED_OUT
                  │            │              │
                  └────────────┴──────────────┴──► CANCELLED

Payment:      PENDING ──► COMPLETED
                      ──► FAILED
                      ──► REFUNDED
```

### Business rules

| Rule | Enforced by |
|---|---|
| `checkOutDate` must be strictly after `checkInDate` | Service layer |
| No two active reservations (PENDING / CONFIRMED / CHECKED_IN) may overlap for the same room | Service + DB query |
| `totalPrice = pricePerNight × nights` — set once at booking, never changed | Entity constructor |
| Exactly one Payment per Reservation | DB UNIQUE constraint on `payments.reservation_id` |
| Deleting a Reservation also deletes its Payment | JPA cascade + `orphanRemoval` |

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
