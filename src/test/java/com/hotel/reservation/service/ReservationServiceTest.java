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
 * TODO [TASK-TESTS-UNIT]: Implement unit tests for ReservationService.book().
 *
 * The mock setup and test fixtures are provided — you write the test bodies.
 *
 * Required scenarios (minimum — add more if you have time):
 *
 *   HAPPY PATH
 *     [ ] A valid request creates and returns a ReservationResponse.
 *         Verify that both reservationRepository.save() and paymentRepository.save()
 *         were called exactly once.
 *
 *   VALIDATION
 *     [ ] checkOutDate equal to checkInDate throws IllegalArgumentException.
 *     [ ] checkOutDate before checkInDate throws IllegalArgumentException.
 *
 *   NOT FOUND
 *     [ ] Unknown guestId throws ResourceNotFoundException.
 *     [ ] Unknown roomId throws ResourceNotFoundException.
 *
 *   CONFLICT
 *     [ ] Overlapping reservation throws RoomNotAvailableException.
 *         Verify that neither reservationRepository.save() nor paymentRepository.save()
 *         was called (no partial writes).
 *
 * Tips:
 *   - Follow the same @Nested class structure as RoomServiceTest.
 *   - Use ArgumentCaptor if you want to assert on what was actually saved.
 *   - verifyNoInteractions() is your friend for asserting nothing was persisted.
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

    // -------------------------------------------------------------------------
    // TODO: write your @Nested test classes here.
    // Start with the happy path, then add failure scenarios one by one.
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("book — happy path")
    class BookHappyPath {

        @Test
        @DisplayName("returns ReservationResponse and persists both reservation and payment")
        void returnsResponseAndPersistsBoth() {
            // TODO
        }
    }

    @Nested
    @DisplayName("book — date validation")
    class BookDateValidation {

        @Test
        @DisplayName("throws IllegalArgumentException when checkOut equals checkIn")
        void throwsWhenDatesAreEqual() {
            // TODO
        }

        @Test
        @DisplayName("throws IllegalArgumentException when checkOut is before checkIn")
        void throwsWhenCheckOutBeforeCheckIn() {
            // TODO
        }
    }

    @Nested
    @DisplayName("book — not found")
    class BookNotFound {

        @Test
        @DisplayName("throws ResourceNotFoundException when guest does not exist")
        void throwsWhenGuestNotFound() {
            // TODO
        }

        @Test
        @DisplayName("throws ResourceNotFoundException when room does not exist")
        void throwsWhenRoomNotFound() {
            // TODO
        }
    }

    @Nested
    @DisplayName("book — conflict")
    class BookConflict {

        @Test
        @DisplayName("throws RoomNotAvailableException and saves nothing when room is already booked")
        void throwsAndSavesNothingWhenRoomTaken() {
            // TODO
        }
    }
}
