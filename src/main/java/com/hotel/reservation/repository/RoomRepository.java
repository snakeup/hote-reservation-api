package com.hotel.reservation.repository;

import com.hotel.reservation.domain.entity.Room;
import com.hotel.reservation.domain.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByAvailableTrue();

    List<Room> findByRoomTypeAndAvailableTrue(RoomType roomType);

    /**
     * Returns rooms that have no overlapping confirmed/pending reservation
     * for the requested period.
     */
    @Query("""
            SELECT r FROM Room r
            WHERE r.available = true
              AND r.id NOT IN (
                  SELECT res.room.id FROM Reservation res
                  WHERE res.status IN ('CONFIRMED', 'CHECKED_IN', 'PENDING')
                    AND res.checkInDate  < :checkOut
                    AND res.checkOutDate > :checkIn
              )
            """)
    List<Room> findAvailableForPeriod(@Param("checkIn") LocalDate checkIn,
                                      @Param("checkOut") LocalDate checkOut);
}
