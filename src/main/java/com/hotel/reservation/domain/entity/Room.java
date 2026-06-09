package com.hotel.reservation.domain.entity;

import com.hotel.reservation.domain.enums.RoomType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false, unique = true, length = 10)
    @NotNull
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    @NotNull
    private RoomType roomType;

    @Column(name = "floor", nullable = false)
    @Min(1)
    private int floor;

    @Column(name = "capacity", nullable = false)
    @Min(1)
    private int capacity;

    @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
    @DecimalMin("0.01")
    @NotNull
    private BigDecimal pricePerNight;

    @Column(name = "available", nullable = false)
    private boolean available = true;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    protected Room() {}

    public Room(String roomNumber, RoomType roomType, int floor, int capacity, BigDecimal pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.floor = floor;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
    }

    public Long getId() { return id; }
    public String getRoomNumber() { return roomNumber; }
    public RoomType getRoomType() { return roomType; }
    public int getFloor() { return floor; }
    public int getCapacity() { return capacity; }
    public BigDecimal getPricePerNight() { return pricePerNight; }
    public boolean isAvailable() { return available; }
    public List<Reservation> getReservations() { return reservations; }

    public void setAvailable(boolean available) { this.available = available; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }

    @Override
    public String toString() {
        return "Room{id=%d, roomNumber='%s', roomType=%s, floor=%d}"
                .formatted(id, roomNumber, roomType, floor);
    }
}
