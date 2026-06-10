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
 * TODO [TASK-TESTS-UNIT]: Unit-test ReservationService.book().
 *
 * These tests run without Spring and without a database. Every collaborator is a
 * Mockito mock, so each test is laser-focused on one behaviour of the booking logic.
 * The setup and fixtures are already provided — you write the tests.
 *
 * Think of the booking flow as a pipeline of guards:
 *   date validation → guest exists → room exists → no overlap → persist both → return response
 * A good test suite verifies that each guard fires correctly and that a failure in one
 * guard leaves no side effects — nothing saved, no payment created.
 *
 * Scenarios to cover (structure and name the tests as you see fit):
 *   - A valid request returns a ReservationResponse and persists both the Reservation and the Payment
 *   - checkOutDate equal to checkInDate is rejected
 *   - checkOutDate before checkInDate is rejected
 *   - An unknown guestId is rejected
 *   - An unknown roomId is rejected
 *   - An overlapping reservation is rejected and nothing is written to the database
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
