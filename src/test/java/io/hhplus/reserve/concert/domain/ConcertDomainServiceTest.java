package io.hhplus.reserve.concert.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ConcertDomainServiceTest {

    @Mock
    private ConcertReader concertReader;

    @InjectMocks
    private ConcertDomainService concertDomainService;

    @Nested
    @DisplayName("예약 가능한 콘서트 목록 조회")
    class ConcertList {

        @Test
        @DisplayName("예약 가능한 콘서트가 존재할 때 리스트 반환")
        void shouldReturnConcertList() {
            // given
            String date = "2024-10-15";
            List<Concert> mockConcertList = List.of(
                    new Concert(1L,
                            "AA Concert",
                            "AA concert desc",
                            LocalDateTime.of(2024, 12, 25, 12, 0),
                            LocalDateTime.of(2024, 12, 25, 16, 0),
                            LocalDateTime.of(2024, 9, 21, 0, 0),
                            LocalDateTime.of(2024, 11, 23, 23, 59)),
                    new Concert(2L,
                            "BB Concert",
                            "BB concert desc",
                            LocalDateTime.of(2024, 12, 25, 12, 0),
                            LocalDateTime.of(2024, 12, 25, 16, 0),
                            LocalDateTime.of(2024, 9, 21, 0, 0),
                            LocalDateTime.of(2024, 11, 23, 23, 59))
            );

            given(concertReader.getConcertList(date)).willReturn(mockConcertList);

            // when
            List<Concert> result = concertDomainService.getAvailableConcertList(date);

            // then
            assertThat(result).hasSize(2);
            assertEquals(result.get(0).getTitle(), "AA Concert");
            assertEquals(result.get(1).getTitle(), "BB Concert");
            then(concertReader).should(times(1)).getConcertList(date);
        }

        @Test
        @DisplayName("예약 가능한 콘서트가 없을 때 빈 리스트 반환")
        void shouldReturnEmptyListWhenNoConcertsAvailable() {
            // given
            String date = "2024-10-15";
            given(concertReader.getConcertList(date)).willReturn(List.of());

            // when
            List<Concert> result = concertDomainService.getAvailableConcertList(date);

            // then
            assertThat(result).isEmpty();
            then(concertReader).should(times(1)).getConcertList(date);
        }
    }

    @Nested
    @DisplayName("콘서트 ID로 좌석 목록 조회")
    class ConcertSeatList {

        @Test
        @DisplayName("콘서트 ID로 예약 가능한 좌석 목록을 반환")
        void shouldReturnSeatList() {
            // given
            Long concertId = 1L;
            List<ConcertSeat> mockSeatList = List.of(
                    new ConcertSeat(1L, concertId, 1, SeatStatus.AVAILABLE, null),
                    new ConcertSeat(2L, concertId, 2, SeatStatus.AVAILABLE, null)
            );

            given(concertReader.getConcertSeatListByConcertId(concertId)).willReturn(mockSeatList);

            // when
            List<ConcertSeat> result = concertDomainService.getSeatListByConcertId(concertId);

            // then
            assertThat(result).hasSize(2);
            assertEquals(result.get(0).getSeatNum(), 1);
            assertEquals(result.get(1).getSeatNum(), 2);
            then(concertReader).should(times(1)).getConcertSeatListByConcertId(concertId);
        }

        @Test
        @DisplayName("콘서트 ID에 예약 가능한 좌석이 없을 때 빈 리스트 반환")
        void shouldReturnEmptySeatList() {
            // given
            Long concertId = 1L;
            given(concertReader.getConcertSeatListByConcertId(concertId)).willReturn(List.of());

            // when
            List<ConcertSeat> result = concertDomainService.getSeatListByConcertId(concertId);

            // then
            assertThat(result).isEmpty();
            then(concertReader).should(times(1)).getConcertSeatListByConcertId(concertId);
        }
    }

}