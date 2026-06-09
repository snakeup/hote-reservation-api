package com.hotel.reservation.controller;

import com.hotel.reservation.domain.entity.Room;
import com.hotel.reservation.domain.enums.RoomType;
import com.hotel.reservation.dto.response.RoomResponse;
import com.hotel.reservation.service.RoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Reference implementation — study this before starting your own controller.
 *
 * Notice:
 *   - Collection-level filters use query params, never path segments.
 *   - A specific resource is identified by a path param: GET /rooms/{id}
 *   - The controller is thin: no business logic, just HTTP mapping.
 *   - Constructor injection, not @Autowired field injection.
 */
@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * GET /rooms
     * Optional filters:
     *   ?type=SUITE
     *   ?checkIn=2025-08-01&checkOut=2025-08-07
     */
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getRooms(
            @RequestParam(required = false) RoomType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut
    ) {
        if (checkIn != null && checkOut != null) {
            return ResponseEntity.ok(roomService.findAvailableForPeriod(checkIn, checkOut));
        }
        if (type != null) {
            return ResponseEntity.ok(roomService.findByType(type));
        }
        // SMELL: findAvailable() returns List<Room> (raw entity) instead of List<RoomResponse>.
        // The unsafe cast below is the symptom — the fix belongs in RoomService.findAvailable().
        // At runtime Jackson will serialize it, but it leaks the domain model through the API.
        @SuppressWarnings("unchecked")
        List<RoomResponse> rooms = (List<RoomResponse>) (List<?>) roomService.findAvailable();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.findById(id));
    }
}
