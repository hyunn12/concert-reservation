package io.hhplus.reserve.concert.interfaces.api;

import io.hhplus.reserve.concert.interfaces.dto.ConcertResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/concert")
@Tag(name = "Concert", description = "콘서트 관련 API")
public class ConcertController {

    @GetMapping("/list/{date}")
    @Operation(summary = "예약 가능한 콘서트 목록 조회", description = "특정 날짜의 예약 가능한 콘서트 목록 조회")
    public List<ConcertResponse.Concert> getList(
            @Parameter(description = "날짜 (형식: yyyy-MM-dd)", example = "2024-12-25", required = true)
            @PathVariable String date
    ) {
        // TODO 날짜별 예약 가능 콘서트 목록 조회 API 작성

        ConcertResponse.Concert concert1 = ConcertResponse.Concert.builder()
                .concertId(1L)
                .title("AA Concert")
                .description("AA Concert Description")
                .concertStartAt(LocalDateTime.of(2024, 12, 25, 12, 0))
                .concertEndAt(LocalDateTime.of(2024, 12, 25, 16, 0))
                .reservationStartAt(LocalDateTime.of(2024, 9, 21, 0, 0))
                .reservationEndAt(LocalDateTime.of(2024, 11, 23, 23, 59))
                .build();
        ConcertResponse.Concert concert2 = ConcertResponse.Concert.builder()
                .concertId(2L)
                .title("BB Concert")
                .description("BB Concert Description")
                .concertStartAt(LocalDateTime.of(2024, 12, 25, 12, 0))
                .concertEndAt(LocalDateTime.of(2024, 12, 25, 16, 0))
                .reservationStartAt(LocalDateTime.of(2024, 9, 21, 0, 0))
                .reservationEndAt(LocalDateTime.of(2024, 11, 23, 23, 59))
                .build();

        return List.of(concert1, concert2);
    }

    @GetMapping("/seat/list/{id}")
    @Operation(summary = "예약 가능한 좌석 목록 조회", description = "특정 콘서트의 예약 가능한 좌석 목록 조회")
    public List<ConcertResponse.Seat> getSeatList(
            @Parameter(description = "콘서트 ID", example = "1", required = true)
            @PathVariable("id") Long concertId
    ) {
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
