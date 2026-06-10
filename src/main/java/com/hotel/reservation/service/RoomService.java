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

    public List<RoomResponse> findAvailable() {
        return roomRepository.findByAvailableTrue()
                .stream()
                .map(RoomResponse::from)
                .toList();
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
