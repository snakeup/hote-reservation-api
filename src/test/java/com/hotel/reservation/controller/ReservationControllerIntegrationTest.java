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

    // TODO: add your test classes here
}
