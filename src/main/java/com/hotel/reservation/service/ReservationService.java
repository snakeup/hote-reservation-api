package com.hotel.reservation.service;

import com.hotel.reservation.domain.entity.Guest;
import com.hotel.reservation.domain.entity.Payment;
import com.hotel.reservation.domain.entity.Reservation;
import com.hotel.reservation.domain.entity.Room;
import com.hotel.reservation.dto.request.CreateReservationRequest;
import com.hotel.reservation.dto.response.ReservationResponse;
import com.hotel.reservation.exception.ResourceNotFoundException;
import com.hotel.reservation.exception.RoomNotAvailableException;
import com.hotel.reservation.repository.GuestRepository;
import com.hotel.reservation.repository.PaymentRepository;
import com.hotel.reservation.repository.ReservationRepository;
import com.hotel.reservation.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final PaymentRepository paymentRepository;

    public ReservationService(ReservationRepository reservationRepository,
                               GuestRepository guestRepository,
                               RoomRepository roomRepository,
                               PaymentRepository paymentRepository) {
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse findById(Long id) {
        return reservationRepository.findById(id)
                .map(ReservationResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    public List<ReservationResponse> findByGuest(Long guestId) {
        if (!guestRepository.existsById(guestId)) {
            throw new ResourceNotFoundException("Guest", guestId);
        }
        return reservationRepository.findReservationsForGuest(guestId)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    /**
     * TODO [TASK-SERVICE]: Implement the reservation booking flow.
     *
     * A guest has selected a room and a date range. Before committing the booking the
     * system must verify that the dates make sense and that no active reservation
     * already holds the room for any part of that period. A room is considered held
     * by any reservation with status PENDING, CONFIRMED, or CHECKED_IN — a mere
     * cancellation is not enough to block a booking, but an in-progress stay is.
     *
     * When the booking is valid, create the Reservation and its linked Payment in a
     * single transaction. If anything fails after the Reservation is saved but before
     * the Payment is saved, both writes must be rolled back — a reservation without a
     * payment must never exist in the database.
     *
     * Steps:
     *
     *   1. checkOutDate must be strictly after checkInDate.
     *      Throw IllegalArgumentException if not.
     *      Think carefully about the equal-date edge case — what does it mean for a
     *      guest to check in and out on the same day?
     *
     *   2. The guest identified by request.guestId() must exist.
     *      Throw ResourceNotFoundException if not.
     *
     *   3. The room identified by request.roomId() must exist.
     *      Throw ResourceNotFoundException if not.
     *
     *   4. No active reservation (PENDING / CONFIRMED / CHECKED_IN) may overlap the
     *      requested period for that room.
     *      Use ReservationRepository.existsOverlappingReservation().
     *      Throw RoomNotAvailableException if an overlap is detected.
     *
     *   5. Create and persist a Reservation (status starts as PENDING).
     *      The Reservation constructor calculates totalPrice — you don't need to.
     *
     *   6. Create and persist a Payment linked to that reservation.
     *      Payment's constructor takes (reservation, amount).
     *      The amount equals the reservation's totalPrice.
     *
     *   7. Return a ReservationResponse mapped from the saved reservation.
     *
     * How does the method-level @Transactional here relate to the class-level
     * @Transactional(readOnly = true)? Why does that distinction matter for steps 5–6?
     */
    @Transactional
    public ReservationResponse book(CreateReservationRequest request) {
        throw new UnsupportedOperationException("Not implemented yet — see TODO [TASK-SERVICE]");
    }
}
