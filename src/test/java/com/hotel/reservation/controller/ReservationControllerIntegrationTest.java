package com.hotel.reservation.controller;

import com.hotel.reservation.dto.response.ReservationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO [TASK-TESTS-INTEGRATION]: Integration-test the Reservation API endpoints.
 *
 * These tests boot the full Spring application against a real PostgreSQL database
 * (spun up by Testcontainers). Flyway runs automatically, applying the schema (V1)
 * and seed data (V2) before any test runs. The container setup and RestClient are
 * already wired — you write the tests. Follow the same patterns as
 * RoomControllerIntegrationTest.
 *
 * Test the endpoints you designed in Phase 2. Use the seed data as the known state:
 *   - Guest 1 (Alice Johnson) has one CONFIRMED reservation on room 1 (SINGLE, 101)
 *     from 2025-09-01 to 2025-09-05.
 *   - Rooms 2–7 and guests 2–3 have no existing reservations — use them for
 *     happy-path and booking tests.
 *
 * Structure and name the tests however you see fit. Think about:
 *   - Happy path for each endpoint (correct data, correct HTTP status)
 *   - Filter combinations for the search endpoint (single filter, multiple filters,
 *     no match, the seed reservation as the known anchor)
 *   - The summary endpoint: does the seed data show up in the expected fields?
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Reservation API — Integration Tests")
class ReservationControllerIntegrationTest {

    static PostgreSQLContainer<?> postgres;

    static {
        postgres = new PostgreSQLContainer<>("postgres:16-alpine")
                .withDatabaseName("hoteldb")
                .withUsername("hotel")
                .withPassword("hotel");
        postgres.start();
    }

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",      postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @LocalServerPort
    int port;

    RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Nested
    @DisplayName("POST /reservations")
    class BookReservation {

        @Test
        @DisplayName("valid booking returns 201, PENDING status, and Location header")
        void validBooking_returns201() {
            ResponseEntity<Map> response = restClient.post()
                    .uri("/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "guestId", 2,
                            "roomId", 2,
                            "checkInDate", "2025-12-01",
                            "checkOutDate", "2025-12-05"))
                    .retrieve()
                    .onStatus(status -> status.isError(), (req, res) -> {})
                    .toEntity(Map.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).containsEntry("status", "PENDING");
            assertThat(response.getBody().get("totalPrice")).isNotNull();
            assertThat(response.getHeaders().getLocation()).isNotNull();
        }

        @Test
        @DisplayName("missing guestId returns 400")
        void missingGuestId_returns400() {
            ResponseEntity<Map> response = restClient.post()
                    .uri("/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "roomId", 3,
                            "checkInDate", "2025-12-01",
                            "checkOutDate", "2025-12-05"))
                    .retrieve()
                    .onStatus(status -> status.isError(), (req, res) -> {})
                    .toEntity(Map.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("missing checkInDate returns 400")
        void missingCheckInDate_returns400() {
            ResponseEntity<Map> response = restClient.post()
                    .uri("/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "guestId", 2,
                            "roomId", 3,
                            "checkOutDate", "2025-12-05"))
                    .retrieve()
                    .onStatus(status -> status.isError(), (req, res) -> {})
                    .toEntity(Map.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("non-existent guestId returns 404")
        void unknownGuest_returns404() {
            ResponseEntity<Map> response = restClient.post()
                    .uri("/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "guestId", 9999,
                            "roomId", 3,
                            "checkInDate", "2025-12-01",
                            "checkOutDate", "2025-12-05"))
                    .retrieve()
                    .onStatus(status -> status.isError(), (req, res) -> {})
                    .toEntity(Map.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("non-existent roomId returns 404")
        void unknownRoom_returns404() {
            ResponseEntity<Map> response = restClient.post()
                    .uri("/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "guestId", 2,
                            "roomId", 9999,
                            "checkInDate", "2025-12-01",
                            "checkOutDate", "2025-12-05"))
                    .retrieve()
                    .onStatus(status -> status.isError(), (req, res) -> {})
                    .toEntity(Map.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("dates overlapping the seed reservation return 409")
        void overlappingDates_returns409() {
            ResponseEntity<Map> response = restClient.post()
                    .uri("/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "guestId", 2,
                            "roomId", 1,
                            "checkInDate", "2025-09-02",
                            "checkOutDate", "2025-09-04"))
                    .retrieve()
                    .onStatus(status -> status.isError(), (req, res) -> {})
                    .toEntity(Map.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
    }

    // TODO: add your test classes here
}
