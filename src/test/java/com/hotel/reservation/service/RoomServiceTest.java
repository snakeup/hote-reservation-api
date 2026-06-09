package com.hotel.reservation.service;

import com.hotel.reservation.domain.entity.Room;
import com.hotel.reservation.domain.enums.RoomType;
import com.hotel.reservation.dto.response.RoomResponse;
import com.hotel.reservation.exception.ResourceNotFoundException;
import com.hotel.reservation.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Reference unit test — study the structure and patterns before writing your own.
 *
 * Notice:
 *   - @ExtendWith(MockitoExtension.class) instead of @SpringBootTest — no Spring context needed.
 *   - Dependencies are mocked with @Mock; the class under test uses @InjectMocks.
 *   - Tests are grouped with @Nested classes, one per method under test.
 *   - Each test has a precise @DisplayName describing the scenario, not the method.
 *   - Assertions use AssertJ (assertThat), not JUnit's assertEquals.
 *   - verify() is used to confirm interactions with mocks, not just return values.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RoomService")
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    private Room singleRoom;
    private Room suiteRoom;

    @BeforeEach
    void setUp() {
        singleRoom = new Room("101", RoomType.SINGLE, 1, 1, new BigDecimal("89.00"));
        suiteRoom  = new Room("301", RoomType.SUITE,  3, 3, new BigDecimal("249.00"));
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("returns all rooms mapped to RoomResponse")
        void returnsAllRoomsMapped() {
            when(roomRepository.findAll()).thenReturn(List.of(singleRoom, suiteRoom));

            List<RoomResponse> result = roomService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result).extracting(RoomResponse::roomType)
                    .containsExactly(RoomType.SINGLE, RoomType.SUITE);
            verify(roomRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("returns empty list when no rooms exist")
        void returnsEmptyListWhenNoRoomsExist() {
            when(roomRepository.findAll()).thenReturn(List.of());

            assertThat(roomService.findAll()).isEmpty();
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("returns mapped RoomResponse when room exists")
        void returnsMappedResponseWhenFound() {
            when(roomRepository.findById(1L)).thenReturn(Optional.of(singleRoom));

            RoomResponse result = roomService.findById(1L);

            assertThat(result.roomNumber()).isEqualTo("101");
            assertThat(result.pricePerNight()).isEqualByComparingTo("89.00");
        }

        @Test
        @DisplayName("throws ResourceNotFoundException when room does not exist")
        void throwsWhenRoomNotFound() {
            when(roomRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> roomService.findById(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");

            verify(roomRepository, times(1)).findById(99L);
        }
    }

    @Nested
    @DisplayName("findAvailableForPeriod")
    class FindAvailableForPeriod {

        @Test
        @DisplayName("returns available rooms for a valid period")
        void returnsAvailableRoomsForValidPeriod() {
            LocalDate checkIn  = LocalDate.of(2025, 10, 1);
            LocalDate checkOut = LocalDate.of(2025, 10, 5);
            when(roomRepository.findAvailableForPeriod(checkIn, checkOut))
                    .thenReturn(List.of(singleRoom));

            List<RoomResponse> result = roomService.findAvailableForPeriod(checkIn, checkOut);

            assertThat(result).hasSize(1);
            assertThat(result.getFirst().roomNumber()).isEqualTo("101");
        }

        @Test
        @DisplayName("throws IllegalArgumentException when checkOut is before checkIn")
        void throwsWhenCheckOutBeforeCheckIn() {
            LocalDate checkIn  = LocalDate.of(2025, 10, 10);
            LocalDate checkOut = LocalDate.of(2025, 10, 5);

            assertThatThrownBy(() -> roomService.findAvailableForPeriod(checkIn, checkOut))
                    .isInstanceOf(IllegalArgumentException.class);

            verifyNoInteractions(roomRepository);
        }

        // NOTE for candidate: there is a subtle bug in validateDateRange().
        // Can you find and fix it? Write a test that exposes it first.
    }
}
