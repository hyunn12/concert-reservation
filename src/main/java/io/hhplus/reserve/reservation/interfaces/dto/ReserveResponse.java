package io.hhplus.reserve.reservation.interfaces.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReserveResponse {

    @Getter
    @Builder
    @Schema(name = "ReserveResponse.Reserve", description = "예약 결과 객체")
    public static class Reserve {

        @Schema(description = "예약 ID", example = "1")
        private Long reservationId;

        @Schema(description = "콘서트명", example = "AA Concert")
        private String concertTitle;

        @Schema(description = "콘서트시작일", example = "2024-12-25 12:00:00")
        private LocalDateTime concertStartAt;

        @Schema(description = "콘서트종료일", example = "2024-12-25 16:00:00")
        private LocalDateTime concertEndAt;

        @Schema(description = "예약상태", example = "SUCCESS")
        private String status;

        @Schema(description = "예약일", example = "2024-10-13 12:00:00")
        private LocalDateTime createdAt;

    }

}
