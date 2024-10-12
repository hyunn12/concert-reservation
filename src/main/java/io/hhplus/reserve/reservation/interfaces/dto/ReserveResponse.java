package io.hhplus.reserve.reservation.interfaces.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReserveResponse {

    @Getter
    @Builder
    public static class Reserve {
        private Long reservationId;
        private Long userId;
        private String concertTitle;
        private LocalDateTime concertDate;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

}
