package io.hhplus.reserve.concert.domain;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class ConcertServiceIntegrationTest {

    @Autowired
    private ConcertService concertService;

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
        assertEquals(50, seatList.size());

        ConcertInfo.SeatDetail seat1 = seatList.get(0);
        ConcertInfo.SeatDetail seat2 = seatList.get(1);

        assertEquals(1, seat1.getSeatNum());
        assertEquals(2, seat2.getSeatNum());
    }

}