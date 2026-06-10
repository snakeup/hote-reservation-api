package com.hotel.reservation.controller;

import com.hotel.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;


/**
 * TODO [TASK-CONTROLLER]: Expose the reservation API over HTTP.
 *
 * Guests and hotel staff need to interact with reservations through a REST API.
 * Staff want to list all reservations or look up one by id. Guests want to see
 * their own reservation history. Anyone can book a new reservation by POSTing
 * to this controller, which delegates all validation and business logic to
 * ReservationService — this controller must stay thin.
 *
 * Required endpoints:
 *
 *   GET  /reservations                    — list all reservations
 *   GET  /reservations/{id}               — single reservation, 404 if not found
 *   GET  /guests/{guestId}/reservations   — all reservations for a specific guest
 *   POST /reservations                    — book a new reservation
 *
 * Things to think about:
 *
 *   - A successful booking should return HTTP 201, not 200. It should also
 *     include a Location header pointing to the new resource so the caller
 *     knows where to retrieve it. How does ServletUriComponentsBuilder help?
 *
 *   - The POST request body must pass Bean Validation before hitting the service.
 *     Which annotation on the method parameter triggers that?
 *
 *   - Why does /guests/{guestId}/reservations make more REST sense than
 *     /reservations?guestId=123? Are there cases where the query-param form
 *     would actually be the better choice?
 *
 *   - Exceptions like ResourceNotFoundException and RoomNotAvailableException
 *     are already handled elsewhere in the codebase — find it before adding
 *     your own try/catch.
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
