package io.hhplus.reserve.concert.interfaces.api;

import io.hhplus.reserve.concert.interfaces.dto.ConcertResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/concert")
public class ConcertController {

    @GetMapping("/list/{date}")
    public List<ConcertResponse.Concert> list(@PathVariable String date) {
        // TODO 날짜별 예약 가능 콘서트 목록 조회 API 작성

        ConcertResponse.Concert concert1 = ConcertResponse.Concert.builder()
                .concertId(1L)
                .title("AA Concert")
                .description("AA Concert Description")
                .concertDate(LocalDateTime.of(2024, 12, 25, 12, 0))
                .reservationStartDate(LocalDateTime.of(2024, 9, 21, 0, 0))
                .reservationEndDate(LocalDateTime.of(2024, 11, 23, 23, 59))
                .build();
        ConcertResponse.Concert concert2 = ConcertResponse.Concert.builder()
                .concertId(2L)
                .title("BB Concert")
                .description("BB Concert Description")
                .concertDate(LocalDateTime.of(2024, 12, 25, 12, 0))
                .reservationStartDate(LocalDateTime.of(2024, 9, 21, 0, 0))
                .reservationEndDate(LocalDateTime.of(2024, 11, 23, 23, 59))
                .build();

        return List.of(concert1, concert2);
    }

    @GetMapping("/seat/list/{id}")
    public List<ConcertResponse.Seat> seatList(@PathVariable("id") Long concertId) {
        // TODO 콘서트별 예약 가능 좌석 목록 조회 API 작성

        ConcertResponse.Seat seat1 = ConcertResponse.Seat.builder()
                .seatId(1L)
                .concertId(concertId)
                .seatNum(1)
                .status("AVAILABLE")
                .build();
        ConcertResponse.Seat seat2 = ConcertResponse.Seat.builder()
                .seatId(2L)
                .concertId(concertId)
                .seatNum(2)
                .status("RESERVED")
                .reservedAt(LocalDateTime.now())
                .build();

        return List.of(seat1, seat2);
    }

}
