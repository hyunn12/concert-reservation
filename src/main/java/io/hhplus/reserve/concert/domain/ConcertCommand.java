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
    public static class Detail {
        private Long concertId;
        private String title;
        private LocalDateTime concertStartAt;
        private LocalDateTime concertEndAt;
    }

}
