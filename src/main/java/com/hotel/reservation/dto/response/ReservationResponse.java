package com.hotel.reservation.dto.response;

import com.hotel.reservation.domain.entity.Reservation;
import com.hotel.reservation.domain.enums.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        GuestResponse guest,
        RoomResponse room,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        long nights,
        ReservationStatus status,
        BigDecimal totalPrice,
        LocalDateTime createdAt
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                GuestResponse.from(reservation.getGuest()),
                RoomResponse.from(reservation.getRoom()),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getNights(),
                reservation.getStatus(),
                reservation.getTotalPrice(),
                reservation.getCreatedAt()
        );
    }
}
