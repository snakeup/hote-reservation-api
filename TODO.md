# TODO — Hotel Reservation API

**Total time: 70 minutes**

Spend the first 5 minutes reading the codebase before writing any code.
The reference implementations (`RoomService`, `RoomController`, `RoomServiceTest`,
`RoomControllerIntegrationTest`) show the patterns and conventions to follow.

---

## Phase 1 — Familiarisation (5 min)

Before touching any code:

- Read `RoomService` and `RoomController` end to end — these are the reference implementations.
- Read `RoomServiceTest` and `RoomControllerIntegrationTest` as test examples.
- Read `ReservationService` — `book()` and `findById()` are already implemented.
  Study how they are written before starting your own work.
- Run the application (`docker compose up -d` then `./mvnw spring-boot:run`)
  and confirm it starts cleanly.
- Run the tests (`./mvnw test`) and confirm the existing ones pass.

---

## Phase 2 — API Design & Controller (20 min)

**File:** `src/main/java/com/hotel/reservation/controller/ReservationController.java`

Design and implement a REST API for the hotel reservation system. You decide the paths,
HTTP methods, query vs path parameters, request and response shapes, and HTTP status codes.
Study `RoomController` for the conventions used in this codebase.

The retrieve-by-id and booking endpoints are already implemented in the controller —
study them as a reference before adding the two new ones.

**Capabilities to implement:**

---

**1. Search reservations with optional filters.**

Hotel staff need to look up reservations without knowing all the details upfront.
The search must support any combination of the following filters — omitting a filter
means "match anything" for that field. All matching reservations are returned ordered
by check-in date, earliest first.

- **Guest** — front-desk staff look up all reservations for a specific guest when
  handling a check-in question or a complaint. Filtering by guest should return
  every reservation that guest has ever made, regardless of status.

- **Status** — operations staff filter by status to manage their workload. For example,
  pulling all CONFIRMED reservations to prepare for upcoming arrivals, or all
  CHECKED_IN reservations to know who is currently in the hotel.

- **Room type** — the revenue team filters by room type (SINGLE, DOUBLE, SUITE,
  PENTHOUSE) to analyse how each category is performing.

- **Date range (`from` / `to`)** — filters on the check-in date, both ends inclusive.
  For example, `from=2025-09-01&to=2025-09-30` returns every reservation whose
  check-in falls within September. Either bound can be omitted independently.

---

**2. Operations dashboard summary.**

The operations team needs a single endpoint that gives them a snapshot of current
reservation activity without having to run multiple queries. The response shape is yours
to define (see `ReservationSummaryResponse`) — think about what a hotel manager would
need to see at a glance to understand the state of the business:

- **Booking counts by status** — how many reservations are in each state right now?
  This tells the duty manager how many check-ins are expected, how many guests are
  currently in the hotel, and how many bookings are awaiting confirmation.

- **Revenue by room type** — total revenue broken down by room category (SINGLE,
  DOUBLE, SUITE, PENTHOUSE). The finance team uses this to understand which room
  types drive the most income and to inform pricing decisions.

- **Average length of stay** — the mean number of nights across all reservations.
  Housekeeping and scheduling use this to plan resources.

- **Total revenue** — the overall revenue sum across all active reservations.
  "Active" means any status other than CANCELLED — a cancelled booking was never
  charged and must not be counted.

**You know you're done when** the app starts and both new endpoints respond correctly.

---

## Phase 3 — Stream Service (20 min)

**File:** `src/main/java/com/hotel/reservation/service/ReservationService.java`

Implement two methods using the Java Stream API:

### `search(Long guestId, ReservationStatus status, RoomType roomType, LocalDate from, LocalDate to)`
All parameters are nullable. A null value means "match anything" — passing only
`guestId=2` should return all of that guest's reservations regardless of other fields.
`from` and `to` are inclusive filters on check-in date. Results sorted by check-in date ascending.

### `summarize()`
Compute what the operations dashboard needs: booking volumes, revenue figures, stay patterns.
"Active" means any status other than CANCELLED — a cancelled booking generated no revenue.

Before implementing `summarize()`, define the fields in `ReservationSummaryResponse`
(`src/main/java/com/hotel/reservation/dto/response/ReservationSummaryResponse.java`) —
that is part of the task.

**You know you're done when** the endpoints from Phase 2 return correct data.

---

## Phase 4 — Tests (25 min)

Quality matters more than quantity.

### Unit tests
**File:** `src/test/java/com/hotel/reservation/service/ReservationServiceTest.java`

Mock setup and fixtures are provided. Write tests for `search()` and `summarize()`.
Structure and name the tests however you see fit.

### Integration tests
**File:** `src/test/java/com/hotel/reservation/controller/ReservationControllerIntegrationTest.java`

Testcontainers and `RestClient` are already wired. Write tests for the endpoints
you designed in Phase 2. Use the seed data as the known starting state:

- Guest 1 (Alice Johnson) has one CONFIRMED reservation on room 1 (SINGLE, 101)
  from 2025-09-01 to 2025-09-05.
- Rooms 2–7 and guests 2–3 have no existing reservations — use them for happy-path tests.

---

## If you finish early

- Look at `ReservationRepository.findReservationsForGuest()` — is this efficient?
- Look at `GlobalExceptionHandler` — what happens to `IllegalArgumentException` today?
  Is that the right HTTP status for a client mistake? How would you fix it?
- Look at `RoomService.validateDateRange()` — is the boundary condition correct?
  Write a test that proves your answer.
