package com.hotel.reservation.dto.response;

import com.hotel.reservation.domain.entity.Guest;

public record GuestResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone
) {
    public static GuestResponse from(Guest guest) {
        return new GuestResponse(
                guest.getId(),
                guest.getFirstName(),
                guest.getLastName(),
                guest.getEmail(),
                guest.getPhone()
        );
    }
}
