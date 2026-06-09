# TODO — Hotel Reservation API

**Total time: 90 minutes**

Spend the first 10 minutes reading the codebase before writing any code.
The reference implementations (`RoomService`, `RoomController`, `RoomServiceTest`,
`RoomControllerIntegrationTest`) show the patterns and conventions to follow.

---

## Phase 1 — Familiarisation (10 min)

Before touching any code:

- Read `RoomService` and `RoomController` end to end.
- Read `V1__create_initial_schema.sql` to understand the data model.
- Read `RoomServiceTest` and `RoomControllerIntegrationTest` as test examples.
- Run the application (`docker compose up -d` then `./mvnw spring-boot:run`)
  and confirm it starts cleanly.
- Run the tests (`./mvnw test`) and confirm the existing ones pass.

---

## Phase 2 — JPA (10 min)

**File:** `src/main/java/com/hotel/reservation/domain/entity/Reservation.java`

Add JPA annotations to every field so Hibernate can map the entity to the
`reservations` table. The schema is already in `V1__create_initial_schema.sql`
— read it first. Hibernate is configured with `ddl-auto=validate`, so it will
fail on startup with a clear error if your annotations don't match.

Fields to annotate:

| Field | Notes |
|---|---|
| `id` | Primary key, auto-generated. Which strategy matches a `BIGSERIAL` column? |
| `guest` | Many-to-one. FK: `guest_id`. Think about fetch strategy at scale. |
| `room` | Many-to-one. FK: `room_id`. Same fetch strategy question. |
| `checkInDate` | Column: `check_in_date`, not nullable. |
| `checkOutDate` | Column: `check_out_date`, not nullable. |
| `status` | Stored as its name (`"CONFIRMED"`), never as its ordinal. |
| `totalPrice` | Column: `total_price`, precision 10, scale 2. |
| `createdAt` | Column: `created_at`. Must never change after the row is inserted. |
| `updatedAt` | Column: `updated_at`. |
| `payment` | Inverse side of a one-to-one. Owning FK is on `payments`. Deleting a reservation must delete its payment. |

**You know you're done when `./mvnw spring-boot:run` starts without Hibernate errors.**

---

## Phase 3 — Service (20 min)

**File:** `src/main/java/com/hotel/reservation/service/ReservationService.java`

Implement the `book(CreateReservationRequest request)` method.

Business rules:

1. `checkOutDate` must be strictly after `checkInDate`. Throw `IllegalArgumentException` if not.
   Think carefully about the equal-date edge case.
2. Guest must exist — throw `ResourceNotFoundException` if not.
3. Room must exist — throw `ResourceNotFoundException` if not.
4. No PENDING, CONFIRMED, or CHECKED_IN reservation may overlap the requested
   period for that room. Use `ReservationRepository.existsOverlappingReservation()`.
   Throw `RoomNotAvailableException` if overlap detected.
5. Persist a new `Reservation` (its constructor calculates `totalPrice` for you).
6. Persist a new `Payment` linked to that reservation.
7. Return a `ReservationResponse`.

Steps 5 and 6 must be **atomic**. If persisting the payment fails, the reservation
must also be rolled back.

---

## Phase 4 — Controller (10 min)

**File:** `src/main/java/com/hotel/reservation/controller/ReservationController.java`

Implement these endpoints:

| Method | Path | Description |
|---|---|---|
| GET | `/reservations` | List all reservations |
| GET | `/reservations/{id}` | Single reservation, 404 if not found |
| GET | `/guests/{guestId}/reservations` | All reservations for a guest |
| POST | `/reservations` | Book a reservation — return **201** with `Location` header |

Follow the same patterns as `RoomController`. The controller must stay thin —
no business logic, just HTTP concerns.

---

## Phase 5 — Tests (40 min)

This is the main part of the exercise. Quality matters more than quantity.

### Unit tests
**File:** `src/test/java/com/hotel/reservation/service/ReservationServiceTest.java`

The class skeleton and fixtures are provided. Implement the test bodies.

Required scenarios for `book()`:

| Scenario | Expected |
|---|---|
| Valid request | Returns `ReservationResponse`, saves reservation AND payment |
| `checkOut` equals `checkIn` | `IllegalArgumentException` |
| `checkOut` before `checkIn` | `IllegalArgumentException` |
| Guest not found | `ResourceNotFoundException` |
| Room not found | `ResourceNotFoundException` |
| Overlapping reservation | `RoomNotAvailableException`, nothing saved |

### Integration tests
**File:** `src/test/java/com/hotel/reservation/controller/ReservationControllerIntegrationTest.java`

The class skeleton, Testcontainers setup, and `@Nested` structure are provided.
Implement the test bodies.

Required scenarios for `POST /reservations`:

| Scenario | Expected HTTP |
|---|---|
| Valid booking | 201 + body with `status: PENDING` + `Location` header |
| Missing `guestId` | 400 |
| Missing `checkInDate` | 400 |
| Non-existent `guestId` | 404 |
| Non-existent `roomId` | 404 |
| Dates overlap existing reservation | 409 |
| `checkOut` equals `checkIn` | ??? — what does it return today, and what *should* it return? |

That last row is intentional — the current codebase returns an unexpected status.
Fix it if you have time, or note it for the discussion.

---

## If you finish early

- Look at `RoomService.findAvailable()` — what does it return? Is that correct?
- Look at `RoomController` — can you spot any issue related to the above?
- Look at `ReservationRepository.findReservationsForGuest()` — is this efficient?
- Look at `GlobalExceptionHandler` — what happens to `IllegalArgumentException` today?
- Look at `RoomService.validateDateRange()` — is the boundary condition correct?
  Write a test that proves your answer.
