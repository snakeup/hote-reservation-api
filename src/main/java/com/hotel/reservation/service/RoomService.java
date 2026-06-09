package com.hotel.reservation.service;

import com.hotel.reservation.domain.entity.Room;
import com.hotel.reservation.domain.enums.RoomType;
import com.hotel.reservation.dto.response.RoomResponse;
import com.hotel.reservation.exception.ResourceNotFoundException;
import com.hotel.reservation.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<RoomResponse> findAll() {
        return roomRepository.findAll()
                .stream()
                .map(RoomResponse::from)
                .toList();
    }

    public RoomResponse findById(Long id) {
        return roomRepository.findById(id)
                .map(RoomResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Room", id));
    }

    // SMELL: returns the raw Room entity instead of a RoomResponse DTO, leaking
    // the domain model out of the service layer. Callers could accidentally mutate
    // or serialize fields that should not be exposed publicly.
    public List<Room> findAvailable() {
        return roomRepository.findByAvailableTrue();
    }

    public List<RoomResponse> findByType(RoomType roomType) {
        return roomRepository.findByRoomTypeAndAvailableTrue(roomType)
                .stream()
                .map(RoomResponse::from)
                .toList();
    }

    public List<RoomResponse> findAvailableForPeriod(LocalDate checkIn, LocalDate checkOut) {
        validateDateRange(checkIn, checkOut);
        return roomRepository.findAvailableForPeriod(checkIn, checkOut)
                .stream()
                .map(RoomResponse::from)
                .toList();
    }

    // SMELL: the condition should be checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn),
    // or equivalently !checkOut.isAfter(checkIn). Using only isBefore accepts same-day
    // check-in/out (0-night stay), which makes no business sense and would produce
    // a totalPrice of zero. This is the kind of subtle off-by-one that unit tests catch.
    private void validateDateRange(LocalDate checkIn, LocalDate checkOut) {
        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
    }

    Room getEntityById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", id));
    }
}
