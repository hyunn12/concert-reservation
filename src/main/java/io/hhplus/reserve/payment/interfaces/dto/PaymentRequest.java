package io.hhplus.reserve.payment.interfaces.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentRequest {

    @Getter
    @Builder
    public static class Payment {
        @NotNull
        private Long userId;

        @NotNull
        private Long reservationId;

        @NotNull
        @Positive
        private int amount;
    }

}
