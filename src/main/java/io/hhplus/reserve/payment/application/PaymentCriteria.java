package io.hhplus.reserve.payment.application;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentCriteria {

    @Getter
    @Builder
    public static class Main {
        private String token;
        private Long userId;
        private Long reservationId;
        private int amount;

        public static PaymentCriteria.Main create(PaymentCommand.Payment command) {
            return PaymentCriteria.Main.builder()
                    .token(command.getToken())
                    .userId(command.getUserId())
                    .reservationId(command.getReservationId())
                    .amount(command.getAmount())
                    .build();
        }
    }

}
