package io.hhplus.reserve.concert.application;

import java.time.LocalDateTime;

public class ConcertCommand {

    public static class Detail {
        private Long concertId;
        private String title;
        private LocalDateTime concertStartAt;
        private LocalDateTime concertEndAt;
    }

}
