package io.hhplus.reserve.concert.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcertCommand {

    @Getter
    @Builder
    public static class Create {
        private String title;
        private String description;
        private LocalDateTime concertStartAt;
        private LocalDateTime concertEndAt;
        private LocalDateTime reservationStartAt;
        private LocalDateTime reservationEndAt;
    }

}
