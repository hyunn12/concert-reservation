package io.hhplus.reserve.payment.interfaces.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentResponse {

    @Getter
    @Builder
    public static class Payment {
        private Long paymentId;
        private Long userId;
        private Long reservationId;
        private int paymentAmount;
        private String status;
        private LocalDateTime createdAt;
    }

}
