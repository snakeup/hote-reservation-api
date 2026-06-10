package com.hotel.reservation.dto.response;


/**
 * TODO [TASK-SUMMARIZE]: Define the fields for the operations dashboard summary.
 *
 * What data does a hotel operations manager need to see at a glance?
 * Consider: booking counts by status, revenue breakdown by room type,
 * average stay length, total revenue. Add the fields that make sense for
 * the dashboard — this is your design decision.
 *
 * Once you've defined the fields, construct this record in ReservationService.summarize().
 */
public record ReservationSummaryResponse() {}
