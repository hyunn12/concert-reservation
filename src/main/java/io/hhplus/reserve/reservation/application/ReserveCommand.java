package io.hhplus.reserve.reservation.application;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReserveCommand {

    @Getter
    @Builder
    public static class Reserve {
        private Long userId;
        private List<Long> seatIdList;
        private LocalDateTime concertDate;
    }

    @Getter
    @Builder
    public static class Reservation {
        private Long userId;
        private List<Long> seatIdList;
        private String concertTitle;
        private LocalDateTime concertStartAt;
        private LocalDateTime concertEndAt;
    }

}
