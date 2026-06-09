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
     * Business rules (read carefully before writing a single line):
     *
     *   1. checkOutDate must be strictly after checkInDate.
     *      Throw IllegalArgumentException if not. Think about the edge case:
     *      what happens if they are equal?
     *
     *   2. The guest identified by request.guestId() must exist.
     *      Throw ResourceNotFoundException if not.
     *
     *   3. The room identified by request.roomId() must exist.
     *      Throw ResourceNotFoundException if not.
     *
     *   4. No existing PENDING, CONFIRMED, or CHECKED_IN reservation may overlap
     *      the requested period for that room.
     *      Use ReservationRepository.existsOverlappingReservation().
     *      Throw RoomNotAvailableException if overlap is detected.
     *
     *   5. Create and persist a Reservation with status PENDING.
     *
     *   6. Create and persist a Payment linked to that reservation.
     *      The payment amount equals the reservation's totalPrice.
     *
     *   7. Return a ReservationResponse mapped from the saved reservation.
     *
     * Transaction requirement:
     *   Steps 5 and 6 must be atomic. If persisting the Payment fails,
     *   the Reservation must also be rolled back.
     *   How does the @Transactional annotation on this method relate to the
     *   readOnly=true on the class-level annotation?
     *
     * Hints:
     *   - Reservation's constructor already calculates totalPrice — you don't need to.
     *   - Payment's constructor takes (reservation, amount).
     *   - Look at how other services load entities — follow the same pattern.
     */
    @Transactional
    public ReservationResponse book(CreateReservationRequest request) {
        throw new UnsupportedOperationException("Not implemented yet — see TODO [TASK-SERVICE]");
    }
}
