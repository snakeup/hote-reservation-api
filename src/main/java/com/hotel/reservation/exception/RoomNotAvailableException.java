package com.hotel.reservation.exception;

public class RoomNotAvailableException extends RuntimeException {

    public RoomNotAvailableException(String roomNumber) {
        super("Room '%s' is not available for the requested period".formatted(roomNumber));
    }
}
