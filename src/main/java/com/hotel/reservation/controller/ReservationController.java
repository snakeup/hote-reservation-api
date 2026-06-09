package com.hotel.reservation.controller;

import com.hotel.reservation.dto.request.CreateReservationRequest;
import com.hotel.reservation.dto.response.ReservationResponse;
import com.hotel.reservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO [TASK-CONTROLLER]: Implement this controller.
 *
 * Required endpoints:
 *
 *   GET  /reservations          — list all reservations
 *   GET  /reservations/{id}     — get a single reservation (404 if not found)
 *   GET  /guests/{guestId}/reservations  — all reservations for a specific guest
 *   POST /reservations          — book a new reservation
 *
 * Things to think about before writing code:
 *
 *   - Why does /guests/{guestId}/reservations make more sense than
 *     /reservations?guestId=123 in REST terms? Are there cases where
 *     the query-param version would be preferable?
 *
 *   - What HTTP status code should POST /reservations return on success?
 *     Should it include a Location header? Look at how Spring's
 *     ServletUriComponentsBuilder can help.
 *
 *   - The request body for POST must be validated. Which annotation on
 *     the method parameter triggers Bean Validation?
 *
 *   - Should this controller handle exceptions directly, or is there
 *     already something in the codebase that does that?
 *
 * Study RoomController first — follow the same patterns.
 */
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // TODO: implement GET /reservations

    // TODO: implement GET /reservations/{id}

    // TODO: implement GET /guests/{guestId}/reservations

    // TODO: implement POST /reservations — return 201 with Location header
}
