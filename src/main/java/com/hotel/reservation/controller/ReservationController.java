package com.hotel.reservation.controller;

import com.hotel.reservation.dto.request.CreateReservationRequest;
import com.hotel.reservation.dto.response.ReservationResponse;
import com.hotel.reservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * TODO [TASK-API]: Add the search and summary endpoints.
 *
 * Required capabilities (see TODO.md Phase 2 for the full requirements):
 *   1. Search reservations — optional filters: guest, status, room type, date range
 *   2. Operations dashboard summary
 *
 * The retrieve-by-id and booking endpoints are already implemented below as reference.
 * You decide the paths, HTTP methods, parameters, and response shapes for the new ones.
 * Study RoomController for conventions.
 */
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> book(@Valid @RequestBody CreateReservationRequest request) {
        ReservationResponse response = reservationService.book(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    // TODO: implement search endpoint
    // TODO: implement summary endpoint
}
