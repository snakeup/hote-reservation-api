package com.hotel.reservation.dto.response;

import com.hotel.reservation.domain.entity.Room;
import com.hotel.reservation.domain.enums.RoomType;

import java.math.BigDecimal;

public record RoomResponse(
        Long id,
        String roomNumber,
        RoomType roomType,
        int floor,
        int capacity,
        BigDecimal pricePerNight,
        boolean available
) {
    public static RoomResponse from(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getFloor(),
                room.getCapacity(),
                room.getPricePerNight(),
                room.isAvailable()
        );
    }
}
