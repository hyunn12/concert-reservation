package io.hhplus.reserve.concert.interfaces.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcertResponse {

    @Getter
    @Builder
    public static class Concert {
        private Long concertId;
        private String title;
        private String description;
        private LocalDateTime concertDate;
        private LocalDateTime reservationStartDate;
        private LocalDateTime reservationEndDate;
    }

    @Getter
    @Builder
    public static class Seat {
        private Long seatId;
        private Long concertId;
        private int seatNum;
        private String status;
        private LocalDateTime reservedAt;
    }

}
