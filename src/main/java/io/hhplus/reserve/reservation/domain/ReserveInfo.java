package io.hhplus.reserve.reservation.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReserveInfo {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Reserve {
        private Long reservationId;
        private String concertTitle;
        private LocalDateTime concertStartAt;
        private LocalDateTime concertEndAt;
        private String status;
        private LocalDateTime createdAt;

        public static Reserve of(Reservation reservation) {
            return new Reserve(
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
