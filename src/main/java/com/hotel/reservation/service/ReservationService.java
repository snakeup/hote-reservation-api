package com.hotel.reservation.service;

import com.hotel.reservation.domain.entity.Guest;
import com.hotel.reservation.domain.entity.Payment;
import com.hotel.reservation.domain.entity.Reservation;
import com.hotel.reservation.domain.entity.Room;
import com.hotel.reservation.domain.enums.ReservationStatus;
import com.hotel.reservation.domain.enums.RoomType;
import com.hotel.reservation.dto.request.CreateReservationRequest;
import com.hotel.reservation.dto.response.ReservationResponse;
import com.hotel.reservation.dto.response.ReservationSummaryResponse;
import com.hotel.reservation.exception.ResourceNotFoundException;
import com.hotel.reservation.exception.RoomNotAvailableException;
import com.hotel.reservation.repository.GuestRepository;
import com.hotel.reservation.repository.PaymentRepository;
import com.hotel.reservation.repository.ReservationRepository;
import com.hotel.reservation.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public ReservationResponse findById(Long id) {
        return reservationRepository.findById(id)
                .map(ReservationResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    /**
     * TODO [TASK-SEARCH]: Implement flexible reservation search using Java Streams.
     *
     * Staff need to filter the reservation list by any combination of guest, status,
     * room type, and date range. A null parameter means "match anything" — passing
     * guestId=2 with everything else null should return only that guest's reservations.
     * Results must be sorted by checkInDate ascending.
     *
     * from/to are inclusive date-range filters on checkInDate.
     */
    public List<ReservationResponse> search(Long guestId, ReservationStatus status,
                                             RoomType roomType, LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("Not implemented yet — see TODO [TASK-SEARCH]");
    }

    /**
     * TODO [TASK-SUMMARIZE]: Compute an aggregated summary using Java Streams.
     *
     * The operations dashboard needs a snapshot of current reservation activity.
     * Think about what a hotel manager would want to see at a glance: booking volumes,
     * revenue figures, stay patterns. "Active" means any status other than CANCELLED —
     * a cancelled booking generated no revenue.
     *
     * Define the fields of ReservationSummaryResponse before implementing this method —
     * that design decision is yours.
     */
    public ReservationSummaryResponse summarize() {
        throw new UnsupportedOperationException("Not implemented yet — see TODO [TASK-SUMMARIZE]");
    }

    @Transactional
    public ReservationResponse book(CreateReservationRequest request) {
        if (!request.checkOutDate().isAfter(request.checkInDate())) {
            throw new IllegalArgumentException(
                    "Check-out date must be strictly after check-in date");
        }

        Guest guest = guestRepository.findById(request.guestId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest", request.guestId()));

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", request.roomId()));

        if (reservationRepository.existsOverlappingReservation(
                room.getId(), request.checkInDate(), request.checkOutDate())) {
            throw new RoomNotAvailableException(room.getRoomNumber());
        }

        Reservation reservation = new Reservation(
                guest, room, request.checkInDate(), request.checkOutDate());
        reservationRepository.save(reservation);

        Payment payment = new Payment(reservation, reservation.getTotalPrice());
        paymentRepository.save(payment);

        return ReservationResponse.from(reservation);
    }
}
