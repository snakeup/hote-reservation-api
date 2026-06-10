package com.hotel.reservation.repository;

import com.hotel.reservation.domain.entity.Reservation;
import com.hotel.reservation.domain.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    default List<Reservation> findReservationsForGuest(Long guestId) {
        return findAll().stream()
                .filter(r -> r.getGuest().getId().equals(guestId))
                .toList();
    }

    List<Reservation> findByStatus(ReservationStatus status);

    /**
     * Returns true if the given room has any PENDING, CONFIRMED, or CHECKED_IN
     * reservation whose date range overlaps [checkIn, checkOut).
     */
    @Query("""
            SELECT COUNT(r) > 0 FROM Reservation r
            WHERE r.room.id = :roomId
              AND r.status IN ('CONFIRMED', 'CHECKED_IN', 'PENDING')
              AND r.checkInDate  < :checkOut
              AND r.checkOutDate > :checkIn
            """)
    boolean existsOverlappingReservation(@Param("roomId") Long roomId,
                                         @Param("checkIn") LocalDate checkIn,
                                         @Param("checkOut") LocalDate checkOut);
}
