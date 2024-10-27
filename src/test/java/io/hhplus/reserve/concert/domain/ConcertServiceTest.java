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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    @Nested
    @DisplayName("예약 가능한 콘서트 목록 조회")
    class ConcertList {

        @Test
        @DisplayName("예약 가능한 콘서트가 존재할 때 리스트 반환")
        void testReturnConcertList() {
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

            given(concertRepository.getConcertList(date)).willReturn(mockConcertList);

            // when
            List<ConcertInfo.ConcertDetail> result = concertService.getAvailableConcertList(date);

            // then
            assertThat(result).hasSize(2);
            assertEquals(result.get(0).getTitle(), "AA Concert");
            assertEquals(result.get(1).getTitle(), "BB Concert");
            then(concertRepository).should(times(1)).getConcertList(date);
        }

        @Test
        @DisplayName("예약 가능한 콘서트가 없을 때 빈 리스트 반환")
        void testReturnEmptyList() {
            // given
            String date = "2024-10-15";
            given(concertRepository.getConcertList(date)).willReturn(List.of());

            // when
            List<ConcertInfo.ConcertDetail> result = concertService.getAvailableConcertList(date);

            // then
            assertThat(result).isEmpty();
            then(concertRepository).should(times(1)).getConcertList(date);
        }
    }

    @Nested
    @DisplayName("콘서트 ID로 좌석 목록 조회")
    class ConcertSeatList {

        @Test
        @DisplayName("콘서트 ID로 예약 가능한 좌석 목록을 반환")
        void testReturnSeatList() {
            // given
            Long concertId = 1L;
            List<ConcertSeat> mockSeatList = List.of(
                    new ConcertSeat(1L, concertId, 1, SeatStatus.AVAILABLE, null),
                    new ConcertSeat(2L, concertId, 2, SeatStatus.AVAILABLE, null)
            );

            given(concertRepository.getConcertSeatListByConcertId(concertId)).willReturn(mockSeatList);

            // when
            List<ConcertInfo.SeatDetail> result = concertService.getSeatListByConcertId(concertId);

            // then
            assertThat(result).hasSize(2);
            assertEquals(result.get(0).getSeatNum(), 1);
            assertEquals(result.get(1).getSeatNum(), 2);
            then(concertRepository).should(times(1)).getConcertSeatListByConcertId(concertId);
        }

        @Test
        @DisplayName("콘서트 ID에 예약 가능한 좌석이 없을 때 빈 리스트 반환")
        void testReturnEmptySeatList() {
            // given
            Long concertId = 1L;
            given(concertRepository.getConcertSeatListByConcertId(concertId)).willReturn(List.of());

            // when
            List<ConcertInfo.SeatDetail> result = concertService.getSeatListByConcertId(concertId);

            // then
            assertThat(result).isEmpty();
            then(concertRepository).should(times(1)).getConcertSeatListByConcertId(concertId);
        }
    }

    @Nested
    @DisplayName("콘서트 상세 조회")
    class ConcertDetail {

        @Test
        @DisplayName("콘서트 ID로 콘서트 상세 조회")
        void testReturnConcertDetail() {
            // given
            Long concertId = 1L;
            Concert concert = new Concert(
                    concertId,
                    "AA Concert",
                    "AA concert description",
                    LocalDateTime.of(2024, 12, 25, 12, 0),
                    LocalDateTime.of(2024, 12, 25, 16, 0),
                    LocalDateTime.of(2024, 9, 21, 0, 0),
                    LocalDateTime.of(2024, 11, 23, 23, 59)
            );

            given(concertRepository.getConcert(concertId)).willReturn(concert);

            // when
            Concert result = concertService.getConcertDetail(concertId);

            // then
            assertNotNull(result);
            assertEquals(result.getConcertId(), concertId);
            assertEquals(result.getTitle(), "AA Concert");
            then(concertRepository).should(times(1)).getConcert(concertId);
        }
    }

    @Nested
    @DisplayName("콘서트 좌석 목록 조회 (락 처리)")
    class ConcertSeatListWithLock {

        @Test
        @DisplayName("콘서트 좌석 목록 조회")
        void testReturnSeatListWithLock() {
            // given
            List<Long> seatIdList = List.of(1L, 2L);
            List<ConcertSeat> mockSeatList = List.of(
                    new ConcertSeat(1L, 1L, 1, SeatStatus.AVAILABLE, null),
                    new ConcertSeat(2L, 1L, 2, SeatStatus.AVAILABLE, null)
            );

            given(concertRepository.getConcertSeatListWithLock(seatIdList)).willReturn(mockSeatList);

            // when
            List<ConcertSeat> result = concertService.getSeatListWithLock(seatIdList);

            // then
            assertThat(result).hasSize(2);
            assertEquals(result.get(0).getSeatId(), 1L);
            assertEquals(result.get(1).getSeatId(), 2L);
            then(concertRepository).should(times(1)).getConcertSeatListWithLock(seatIdList);
        }
    }

}