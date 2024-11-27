package io.hhplus.reserve.payment.domain;

import io.hhplus.reserve.reservation.domain.Reservation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Main implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private Long reservationId;
        private Long paymentId;
        private Long userId;
        private int paymentAmount;
        private String concertTitle;
        private LocalDateTime concertStartAt;
        private LocalDateTime concertEndAt;
        private String status;
        private LocalDateTime createAt;

        public static Main of(Payment payment, Reservation reservation) {
            return new Main(
                    reservation.getReservationId(),
                    payment.getPaymentId(),
                    payment.getUserId(),
                    payment.getPaymentAmount(),
                    reservation.getConcertTitle(),
                    reservation.getConcertStartAt(),
                    reservation.getConcertEndAt(),
                    reservation.getStatus().toString(),
                    payment.getCreatedAt()
            );
        }
    }

}
