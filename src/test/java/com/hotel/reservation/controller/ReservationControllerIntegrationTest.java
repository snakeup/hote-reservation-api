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
 * TODO [TASK-TESTS-INTEGRATION]: Implement integration tests for POST /reservations.
 *
 * The container setup is provided — you write the test bodies.
 * Follow the exact same patterns as RoomControllerIntegrationTest.
 *
 * To POST a JSON body use:
 *   ResponseEntity<ReservationResponse> response = restClient.post()
 *       .uri("/reservations")
 *       .contentType(MediaType.APPLICATION_JSON)
 *       .body(Map.of("guestId", 2, "roomId", 2,
 *                    "checkInDate", "2025-12-01", "checkOutDate", "2025-12-05"))
 *       .retrieve()
 *       .onStatus(status -> status.isError(), (req, res) -> {})
 *       .toEntity(ReservationResponse.class);
 *
 * Required scenarios:
 *
 *   HAPPY PATH — HTTP 201
 *     [ ] Valid booking returns 201 with status "PENDING" and non-null totalPrice.
 *         Assert a Location header is present pointing to the new resource.
 *         Use guests 2 or 3 and rooms 2-7 (no existing reservations in seed data).
 *         Use future dates to avoid overlap with seed data.
 *
 *   VALIDATION — HTTP 400
 *     [ ] Missing guestId returns 400.
 *     [ ] Missing checkInDate returns 400.
 *
 *   NOT FOUND — HTTP 404
 *     [ ] Non-existent guestId (e.g. 9999) returns 404.
 *     [ ] Non-existent roomId (e.g. 9999) returns 404.
 *
 *   CONFLICT — HTTP 409
 *     [ ] Booking room 1 for 2025-09-01 to 2025-09-05 overlaps the seed reservation — returns 409.
 *
 *   DATE VALIDATION — what status does it return today?
 *     [ ] checkOutDate equal to checkInDate.
 *         Look at GlobalExceptionHandler. What does IllegalArgumentException map to?
 *         Is that correct? What would you change?
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("POST /reservations — Integration Tests")
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
    @DisplayName("happy path")
    class HappyPath {

        @Test
        @DisplayName("valid booking returns HTTP 201 with ReservationResponse and Location header")
        void validBooking_returns201() {
            // TODO
        }
    }

    @Nested
    @DisplayName("validation errors")
    class ValidationErrors {

        @Test
        @DisplayName("missing guestId returns HTTP 400")
        void missingGuestId_returns400() {
            // TODO
        }

        @Test
        @DisplayName("missing checkInDate returns HTTP 400")
        void missingCheckInDate_returns400() {
            // TODO
        }
    }

    @Nested
    @DisplayName("not found")
    class NotFound {

        @Test
        @DisplayName("unknown guestId returns HTTP 404")
        void unknownGuest_returns404() {
            // TODO
        }

        @Test
        @DisplayName("unknown roomId returns HTTP 404")
        void unknownRoom_returns404() {
            // TODO
        }
    }

    @Nested
    @DisplayName("conflict")
    class Conflict {

        @Test
        @DisplayName("overlapping dates return HTTP 409")
        void overlappingDates_returns409() {
            // TODO
        }
    }
}
