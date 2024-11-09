package io.hhplus.reserve.concert.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcertInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ConcertDetail {

        private Long concertId;
        private String title;
        private String description;
        private LocalDateTime concertStartAt;
        private LocalDateTime concertEndAt;
        private LocalDateTime reservationStartAt;
        private LocalDateTime reservationEndAt;

        public static ConcertDetail of(Concert concert) {
            return new ConcertDetail(
                    concert.getConcertId(),
                    concert.getTitle(),
                    concert.getDescription(),
                    concert.getConcertStartAt(),
                    concert.getConcertEndAt(),
                    concert.getReservationStartAt(),
                    concert.getReservationEndAt()
            );
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SeatDetail {

        private Long seatId;
        private Long concertId;
        private int seatNum;
        private String status;
        private LocalDateTime reservedAt;

        public static SeatDetail of(ConcertSeat seat) {
            return new SeatDetail(
                    seat.getSeatId(),
                    seat.getConcertId(),
                    seat.getSeatNum(),
                    seat.getStatus().toString(),
                    seat.getReservedAt()
            );
        }
    }
}
