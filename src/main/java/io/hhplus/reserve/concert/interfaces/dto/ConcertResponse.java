package io.hhplus.reserve.concert.interfaces.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcertResponse {

    @Getter
    @Builder
    @Schema(name = "ConcertResponse.Concert", description = "콘서트 조회 결과 객체")
    public static class Concert {

        @Schema(description = "콘서트 ID", example = "1")
        private Long concertId;

        @Schema(description = "제목", example = "AA Concert")
        private String title;

        @Schema(description = "설명", example = "AA Concert Description")
        private String description;

        @Schema(description = "콘서트시작일", example = "2024-12-25 12:00:00")
        private LocalDateTime concertStartAt;

        @Schema(description = "콘서트종료일", example = "2024-12-25 16:00:00")
        private LocalDateTime concertEndAt;

        @Schema(description = "예약시작일", example = "2024-09-21 00:00:00")
        private LocalDateTime reservationStartAt;

        @Schema(description = "예약종료일", example = "2024-11-23 23:59:59")
        private LocalDateTime reservationEndAt;

    }

    @Getter
    @Builder
    @Schema(name = "ConcertResponse.Seat", description = "콘서트 좌석 조회 결과 객체")
    public static class Seat {

        @Schema(description = "좌석 ID", example = "1")
        private Long seatId;

        @Schema(description = "콘서트 ID", example = "1")
        private Long concertId;

        @Schema(description = "좌석번호", example = "1")
        private int seatNum;

        @Schema(description = "좌석상태", example = "AVAILABLE")
        private String status;

        @Schema(description = "예약일", example = "2024-10-13 12:00:00")
        private LocalDateTime reservedAt;

    }

}
