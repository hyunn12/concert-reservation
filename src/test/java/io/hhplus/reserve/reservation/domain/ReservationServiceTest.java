package io.hhplus.reserve.reservation.domain;

import io.hhplus.reserve.concert.domain.Concert;
import io.hhplus.reserve.concert.domain.ConcertSeat;
import io.hhplus.reserve.concert.domain.SeatStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReserveRepository reserveRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Concert mockConcert;
    private List<ConcertSeat> mockSeatList;

    @BeforeEach
    void setUp() {
        long concertId = 1L;
        String concertTitle = "X-mas Concert";
        String desc = "Concert Description";
        LocalDateTime concertStartAt = LocalDateTime.of(2024, 12, 25, 12, 0);
        LocalDateTime concertEndAt = LocalDateTime.of(2024, 12, 25, 16, 0);
        LocalDateTime reservationStartAt = LocalDateTime.of(2024, 9, 21, 0, 0);
        LocalDateTime reservationEndAt = LocalDateTime.of(2024, 11, 23, 23, 59);
        mockConcert = new Concert(concertId, concertTitle, desc, concertStartAt, concertEndAt, reservationStartAt, reservationEndAt);

        ConcertSeat seat1 = new ConcertSeat(1L, concertId, 1, SeatStatus.AVAILABLE, null);
        ConcertSeat seat2 = new ConcertSeat(2L, concertId, 2, SeatStatus.AVAILABLE, null);
        ConcertSeat seat3 = new ConcertSeat(3L, concertId, 3, SeatStatus.AVAILABLE, null);
        mockSeatList = List.of(seat1, seat2, seat3);
    }

    @Nested
    @DisplayName("예약 생성 테스트")
    class Reserve {

        @Test
        @DisplayName("정상적으로 예약 생성")
        void createReservation() {
            // given
            Long userId = 1L;
            ReserveCriteria.Reserve criteria = ReserveCriteria.Reserve.builder()
                    .userId(userId)
                    .concert(mockConcert)
                    .seatList(mockSeatList)
                    .build();

            Reservation reservation = new Reservation(1L,
                    userId,
                    criteria.getConcert().getTitle(),
                    criteria.getConcert().getConcertStartAt(),
                    criteria.getConcert().getConcertEndAt(),
                    ReservationStatus.SUCCESS
            );

            given(reserveRepository.generateReservation(any(Reservation.class))).willReturn(reservation);

            // when
            Reservation result = reservationService.reserve(criteria);

            // then
            assertNotNull(result);
            assertEquals(result.getConcertTitle(), mockConcert.getTitle());
            assertEquals(result.getConcertStartAt(), mockConcert.getConcertStartAt());
            assertEquals(result.getConcertEndAt(), mockConcert.getConcertEndAt());

            then(reserveRepository).should(times(1)).generateReservation(any(Reservation.class));
            then(reserveRepository).should(times(1)).generateReservationItemList(anyList());
        }

        @Test
        @DisplayName("예약 항목 추가 테스트")
        void createReservationItems() {
            // given
            Long userId = 1L;
            ReserveCriteria.Reserve criteria = ReserveCriteria.Reserve.builder()
                    .userId(userId)
                    .concert(mockConcert)
                    .seatList(mockSeatList)
                    .build();

            Reservation reservation = new Reservation(1L, userId, criteria.getConcert().getTitle(), criteria.getConcert().getConcertStartAt(), criteria.getConcert().getConcertEndAt(), ReservationStatus.SUCCESS);

            given(reserveRepository.generateReservation(any(Reservation.class))).willReturn(reservation);

            // when
            reservationService.reserve(criteria);

            // then
            then(reserveRepository).should(times(1)).generateReservationItemList(anyList());
        }

    }

}