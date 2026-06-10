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
 * TODO [TASK-TESTS-INTEGRATION]: Integration-test POST /reservations.
 *
 * These tests boot the full Spring application against a real PostgreSQL database
 * (spun up by Testcontainers). Flyway runs automatically, applying the schema (V1)
 * and seed data (V2) before any test runs. The container setup and RestClient are
 * already wired — you write the tests. Follow the same patterns as
 * RoomControllerIntegrationTest.
 *
 * Seed data to know:
 *   - Room 1 (101, SINGLE) has a CONFIRMED reservation for guest 1 from 2025-09-01
 *     to 2025-09-05. Use this range to trigger the conflict scenario.
 *   - Rooms 2–7 and guests 2–3 have no existing reservations — use them for
 *     happy-path and other tests.
 *
 * To POST a JSON body:
 *   ResponseEntity<ReservationResponse> response = restClient.post()
 *       .uri("/reservations")
 *       .contentType(MediaType.APPLICATION_JSON)
 *       .body(Map.of("guestId", 2, "roomId", 2,
 *                    "checkInDate", "2025-12-01", "checkOutDate", "2025-12-05"))
 *       .retrieve()
 *       .onStatus(status -> status.isError(), (req, res) -> {})
 *       .toEntity(ReservationResponse.class);
 *
 * Scenarios to cover (structure and name the tests as you see fit):
 *   - A valid booking returns 201, status "PENDING", a non-null totalPrice, and a Location header
 *   - Missing required fields (e.g. guestId, checkInDate) return 400
 *   - A non-existent guestId returns 404
 *   - A non-existent roomId returns 404
 *   - Dates that overlap the seed reservation return 409
 *   - checkOutDate equal to checkInDate — what status does the API return today?
 *     Is that the right status for a client mistake? What would you change?
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

    // TODO: add your test classes here
}
