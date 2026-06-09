package com.hotel.reservation.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Reference integration test — study this before writing your own.
 *
 * Key setup decisions:
 *   - @SpringBootTest(RANDOM_PORT) boots the full application on a random port.
 *   - PostgreSQLContainer is started manually in @BeforeAll and stopped in @AfterAll,
 *     giving one shared database per test class (faster than per-test).
 *   - @DynamicPropertySource wires the container's JDBC URL into Spring's datasource config.
 *   - Flyway runs automatically applying V1 (schema) and V2 (seed data).
 *   - RestClient is built in @BeforeEach using the injected @LocalServerPort.
 *   - AssertJ (assertThat) for all assertions — not assertEquals.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("GET /rooms — Integration Tests")
class RoomControllerIntegrationTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("hoteldb")
            .withUsername("hotel")
            .withPassword("hotel");

    @BeforeAll
    static void startContainer() {
        postgres.start();
    }

    @AfterAll
    static void stopContainer() {
        postgres.stop();
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

    @Test
    @DisplayName("returns all seeded rooms with HTTP 200")
    void getAllRooms_returns200WithAllRooms() {
        ResponseEntity<List> response = restClient.get()
                .uri("/rooms")
                .retrieve()
                .toEntity(List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSizeGreaterThanOrEqualTo(7);
    }

    @Test
    @DisplayName("returns the correct room for a known id")
    void getRoomById_returnsCorrectRoom() {
        ResponseEntity<Map> response = restClient.get()
                .uri("/rooms/1")
                .retrieve()
                .toEntity(Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .containsEntry("roomNumber", "101")
                .containsEntry("roomType", "SINGLE");
    }

    @Test
    @DisplayName("returns HTTP 404 for an unknown room id")
    void getRoomById_returns404ForUnknownId() {
        ResponseEntity<String> response = restClient.get()
                .uri("/rooms/9999")
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), (req, res) -> {})
                .toEntity(String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("returns only SUITE rooms when filtered by type")
    void getRooms_filtersCorrectlyByType() {
        ResponseEntity<List> response = restClient.get()
                .uri("/rooms?type=SUITE")
                .retrieve()
                .toEntity(List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rooms = (List<Map<String, Object>>) response.getBody();
        rooms.forEach(room -> assertThat(room).containsEntry("roomType", "SUITE"));
    }
}
