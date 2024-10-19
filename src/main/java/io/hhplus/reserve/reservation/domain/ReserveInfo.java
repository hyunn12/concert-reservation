package io.hhplus.reserve.reservation.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReserveInfo {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Reserve {
        private Long userId;
        private List<Long> seatIdList;
        private String token;

        public static Reserve of(Long userId, List<Long> seatIdList, String token) {
            return new Reserve(userId, seatIdList, token);
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Main {
        private Long reservationId;
        private String concertTitle;
        private LocalDateTime concertStartAt;
        private LocalDateTime concertEndAt;
        private String status;
        private LocalDateTime createdAt;

        public static Main of(Reservation reservation) {
            return new Main(
                    reservation.getReservationId(),
                    reservation.getConcertTitle(),
                    reservation.getConcertStartAt(),
                    reservation.getConcertEndAt(),
                    reservation.getStatus().toString(),
                    reservation.getCreatedAt()
            );
        }
    }

}
