package io.hhplus.reserve.reservation.interfaces.api;

import io.hhplus.reserve.reservation.interfaces.dto.ReserveRequest;
import io.hhplus.reserve.reservation.interfaces.dto.ReserveResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reserve")
public class ReserveController {

    @PostMapping("/reserve")
    public ReserveResponse.Reserve reserve(@RequestBody ReserveRequest.Reserve request) {
        // TODO 좌석 예약 요청 API 작성

        return ReserveResponse.Reserve.builder()
                .reservationId(1L)
                .userId(request.getUserId())
                .concertTitle("AA Concert")
                .concertDate(LocalDateTime.of(2024, 12, 25, 12, 0))
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}
