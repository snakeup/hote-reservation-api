package com.hotel.reservation.service;

import com.hotel.reservation.domain.entity.Guest;
import com.hotel.reservation.domain.entity.Payment;
import com.hotel.reservation.domain.entity.Reservation;
import com.hotel.reservation.domain.entity.Room;
import com.hotel.reservation.domain.enums.RoomType;
import com.hotel.reservation.dto.request.CreateReservationRequest;
import com.hotel.reservation.dto.response.ReservationResponse;
import com.hotel.reservation.exception.ResourceNotFoundException;
import com.hotel.reservation.exception.RoomNotAvailableException;
import com.hotel.reservation.repository.GuestRepository;
import com.hotel.reservation.repository.PaymentRepository;
import com.hotel.reservation.repository.ReservationRepository;
import com.hotel.reservation.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * TODO [TASK-TESTS-UNIT]: Unit-test ReservationService.search() and summarize().
 *
 * These tests run without Spring and without a database. Every collaborator is a
 * Mockito mock, so each test is laser-focused on one behaviour of the data processing
 * logic. The mock setup and fixtures are provided — you write the tests.
 *
 * For both methods, mock reservationRepository.findAll() to return a hand-crafted
 * list of Reservation objects that covers the scenarios you want to test.
 * Structure and name the tests however you see fit.
 *
 * ── search() ────────────────────────────────────────────────────────────────
 * Build a fixture list that spans multiple guests, statuses, room types, and dates.
 * Then drive the search with different parameter combinations and assert on the
 * contents and ordering of the returned list.
 *
 * Scenarios to consider:
 *   - All-null params returns every reservation, sorted by checkInDate
 *   - Filtering by a single field (guestId, status, roomType, from, to) narrows correctly
 *   - Combining multiple filters applies all of them (intersection, not union)
 *   - No match returns an empty list without throwing
 *
 * ── summarize() ─────────────────────────────────────────────────────────────
 * Build a fixture list that includes at least one CANCELLED reservation and at
 * least one of each active status to make the assertions meaningful.
 *
 * Scenarios to consider:
 *   - CANCELLED reservations are excluded from revenue totals
 *   - countByStatus has an entry for every status present in the fixture data
 *   - Revenue is zero when all reservations are CANCELLED
 *   - averageNights is the arithmetic mean of getNights() across all reservations
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReservationService")
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private ReservationService reservationService;

    // --- Fixtures ---

    private Guest alice;
    private Room room101;

    private final LocalDate checkIn  = LocalDate.now().plusDays(10);
    private final LocalDate checkOut = LocalDate.now().plusDays(14);

    @BeforeEach
    void setUp() {
        alice   = new Guest("Alice", "Johnson", "alice@example.com", "+1-555-0101");
        room101 = new Room("101", RoomType.SINGLE, 1, 1, new BigDecimal("89.00"));
    }

    private CreateReservationRequest validRequest() {
        return new CreateReservationRequest(1L, 1L, checkIn, checkOut);
    }

    // TODO: add your test classes here
}
