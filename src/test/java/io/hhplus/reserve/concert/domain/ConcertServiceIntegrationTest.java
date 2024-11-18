package io.hhplus.reserve.concert.domain;

import io.hhplus.reserve.concert.infra.ConcertJpaRepository;
import io.hhplus.reserve.concert.infra.ConcertSeatJpaRepository;
import io.hhplus.reserve.config.TestContainerSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class ConcertServiceIntegrationTest extends TestContainerSupport {

    // orm --
    @Autowired
    private ConcertJpaRepository concertJpaRepository;
    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;

    // sut --
    @Autowired
    private ConcertService concertService;

    List<Concert> concerts;
    List<ConcertSeat> concertSeats;

    @BeforeEach
    void setUp() {
        concerts = List.of(
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
        concertJpaRepository.saveAll(concerts);

        concertSeats = List.of(
                new ConcertSeat(1L, 1L, 1, SeatStatus.AVAILABLE, null, 0L),
                new ConcertSeat(2L, 1L, 2, SeatStatus.AVAILABLE, null, 0L),
                new ConcertSeat(3L, 1L, 3, SeatStatus.AVAILABLE, null, 0L),
                new ConcertSeat(4L, 1L, 4, SeatStatus.AVAILABLE, null, 0L)
        );
        concertSeatJpaRepository.saveAll(concertSeats);
    }


    @Test
    @DisplayName("콘서트 목록 조회")
    void testGetAvailableConcertList() {
        String date = "2024-10-15";
        List<ConcertInfo.ConcertDetail> concertList = concertService.getAvailableConcertList(date);

        assertNotNull(concertList);
        assertEquals(2, concertList.size());
    }

    @Test
    @DisplayName("좌석 목록 조회")
    void testGetConcertSeatList() {
        Long concertId = 1L;

        List<ConcertInfo.SeatDetail> seatList = concertService.getSeatListByConcertId(concertId);

        assertNotNull(seatList);
        assertEquals(concertSeats.size(), seatList.size());

        ConcertInfo.SeatDetail seat1 = seatList.get(0);
        ConcertInfo.SeatDetail seat2 = seatList.get(1);

        assertEquals(concertSeats.get(0).getSeatNum(), seat1.getSeatNum());
        assertEquals(concertSeats.get(1).getSeatNum(), seat2.getSeatNum());
    }

}