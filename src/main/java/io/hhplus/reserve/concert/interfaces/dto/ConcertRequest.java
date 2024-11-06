package io.hhplus.reserve.concert.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.hhplus.reserve.concert.domain.ConcertCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcertRequest {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Schema(name = "ConcertRequest.Create", description = "콘서트 생성 요청 객체")
    public static class Create {

        @NotNull
        @Schema(description = "콘서트명", example = "X-mas Concert")
        private String title;

        @Schema(description = "콘서트 설명", example = "concert description")
        private String description;

        @Schema(description = "콘서트시작일", example = "2024-12-25 12:00:00")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime concertStartAt;

        @Schema(description = "콘서트종료일", example = "2024-12-25 16:00:00")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime concertEndAt;

        @Schema(description = "콘서트예약시작일", example = "2024-11-11 00:00:00")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime reservationStartAt;

        @Schema(description = "콘서트예약종료일", example = "2024-11-14 23:59:59")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime reservationEndAt;

        public ConcertCommand.Create toCommand() {
            return ConcertCommand.Create.builder()
                    .title(title)
                    .description(description)
                    .concertStartAt(concertStartAt)
                    .concertEndAt(concertEndAt)
                    .reservationStartAt(reservationStartAt)
                    .reservationEndAt(reservationEndAt)
                    .build();
        }
    }
}
