package io.hhplus.reserve.reservation.domain;

import io.hhplus.reserve.reservation.application.ReserveCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ReservationDomainServiceTest {

    @Mock
    private ReserveStore reserveStore;

    @InjectMocks
    private ReservationDomainService reservationDomainService;

    @Nested
    @DisplayName("예약 생성 테스트")
    class Reserve {

        @Test
        @DisplayName("정상적으로 예약 생성")
        void createReservation() {
            // given
            Long userId = 1L;
            String concertTitle = "X-mas Concert";
            LocalDateTime concertStartAt = LocalDateTime.of(2024, 12, 25, 12, 0);
            LocalDateTime concertEndAt = LocalDateTime.of(2024, 12, 25, 16, 0);

            List<Long> seatIdList = List.of(1L, 2L, 3L);

            ReserveCommand.Reservation command = ReserveCommand.Reservation.builder()
                    .userId(userId)
                    .concertTitle(concertTitle)
                    .concertStartAt(concertStartAt)
                    .concertEndAt(concertEndAt)
                    .seatIdList(seatIdList)
                    .build();

            Reservation reservation = new Reservation(1L, userId, concertTitle, concertStartAt, concertEndAt, ReservationStatus.SUCCESS);

            given(reserveStore.generateReservation(any(Reservation.class))).willReturn(reservation);

            // when
            Reservation result = reservationDomainService.reserve(command);

            // then
            assertNotNull(result);
            assertEquals(result.getUserId(), userId);
            assertEquals(result.getConcertTitle(), concertTitle);
            assertEquals(result.getConcertStartAt(), concertStartAt);
            assertEquals(result.getConcertEndAt(), concertEndAt);

            then(reserveStore).should(times(1)).generateReservation(any(Reservation.class));
            then(reserveStore).should(times(seatIdList.size())).generateReservationItem(any(ReservationItem.class));
        }

        @Test
        @DisplayName("예약 항목 추가 테스트")
        void createReservationItems() {
            // given
            Long userId = 1L;
            String concertTitle = "X-mas Concert";
            LocalDateTime concertStartAt = LocalDateTime.of(2024, 12, 25, 12, 0);
            LocalDateTime concertEndAt = LocalDateTime.of(2024, 12, 25, 16, 0);
            List<Long> seatIdList = List.of(1L, 2L, 3L);

            ReserveCommand.Reservation command = ReserveCommand.Reservation.builder()
                    .userId(userId)
                    .concertTitle(concertTitle)
                    .concertStartAt(concertStartAt)
                    .concertEndAt(concertEndAt)
                    .seatIdList(seatIdList)
                    .build();

            Reservation reservation = new Reservation(1L, userId, concertTitle, concertStartAt, concertEndAt, ReservationStatus.SUCCESS);

            given(reserveStore.generateReservation(any(Reservation.class))).willReturn(reservation);

            // when
            reservationDomainService.reserve(command);

            // then
            then(reserveStore).should(times(seatIdList.size())).generateReservationItem(any(ReservationItem.class));
        }

    }

}