package com.hotel.reservation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateReservationRequest(
        @NotNull(message = "Guest ID is required")
        Long guestId,

        @NotNull(message = "Room ID is required")
        Long roomId,

        @NotNull(message = "Check-in date is required")
        LocalDate checkInDate,

        @NotNull(message = "Check-out date is required")
        LocalDate checkOutDate
) {}
