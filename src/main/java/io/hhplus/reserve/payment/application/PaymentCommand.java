package io.hhplus.reserve.payment.application;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentCommand {

    @Getter
    @Builder
    public static class Payment {
        private String token;
        private Long userId;
        private Long reservationId;
        private int amount;
    }

}
