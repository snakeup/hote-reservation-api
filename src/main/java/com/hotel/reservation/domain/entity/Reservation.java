package com.hotel.reservation.domain.entity;

import com.hotel.reservation.domain.enums.ReservationStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * TODO [TASK-JPA]: This entity is not yet mapped to the database.
 * Add the necessary JPA annotations to each field so Hibernate can persist
 * and load reservations correctly.
 *
 * The target table is "reservations". Its schema is defined in V1__create_initial_schema.sql
 * — read it before you start. Hibernate is configured with ddl-auto=validate, meaning it will
 * compare your annotations against the real schema on startup and fail fast if they diverge.
 *
 * Fields to annotate and things to consider for each:
 *
 *   id            — primary key; which generation strategy matches a BIGSERIAL column?
 *
 *   guest         — many reservations belong to one guest (FK: guest_id, not nullable).
 *                   Should this be loaded eagerly or lazily? What is the risk of the
 *                   wrong choice when loading a list of 10 000 reservations?
 *
 *   room          — many reservations belong to one room (FK: room_id, not nullable).
 *                   Same fetch strategy question as guest.
 *
 *   checkInDate   — column: check_in_date, not nullable.
 *   checkOutDate  — column: check_out_date, not nullable.
 *
 *   status        — stored as its name (e.g. "CONFIRMED"), never as its ordinal.
 *                   Column: status, not nullable.
 *                   Which annotation controls string vs ordinal storage?
 *
 *   totalPrice    — column: total_price, not nullable, precision 10 scale 2.
 *
 *   createdAt     — column: created_at, not nullable.
 *                   This value must be set once at insert time and never changed.
 *                   Which @Column attribute enforces that at the ORM level?
 *
 *   updatedAt     — column: updated_at.
 *
 *   payment       — one reservation has at most one payment.
 *                   The foreign key lives on the payments table (Payment is the owning side).
 *                   If a reservation is deleted its payment must be deleted too.
 *                   Which combination of cascade and attribute on @OneToOne achieves that?
 */
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    // -------------------------------------------------------------------------
    // Everything below is provided — do not modify.
    // -------------------------------------------------------------------------

    protected Reservation() {}

    public Reservation(Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = ReservationStatus.PENDING;
        this.totalPrice = calculateTotalPrice(room.getPricePerNight(), checkInDate, checkOutDate);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public long getNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    private static BigDecimal calculateTotalPrice(BigDecimal pricePerNight,
                                                   LocalDate checkIn,
                                                   LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return pricePerNight.multiply(BigDecimal.valueOf(nights));
    }

    public Long getId() { return id; }
    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public ReservationStatus getStatus() { return status; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Payment getPayment() { return payment; }

    public void setStatus(ReservationStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Reservation{id=%d, guestId=%d, roomId=%d, checkIn=%s, checkOut=%s, status=%s}"
                .formatted(id, guest.getId(), room.getId(), checkInDate, checkOutDate, status);
    }
}
