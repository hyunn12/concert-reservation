package io.hhplus.reserve.payment.application;

import io.hhplus.reserve.payment.domain.Payment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentInfo {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Main {
        private Long userId;
        private int paymentAmount;
        private String status;
        private LocalDateTime createAt;

        public static Main of(Payment payment) {
            return new Main(
                    payment.getUserId(),
                    payment.getPaymentAmount(),
                    payment.getStatus().toString(),
                    payment.getCreatedAt()
            );
        }
    }

}
