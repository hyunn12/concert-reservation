package io.hhplus.reserve.payment.interfaces.dto;

import io.hhplus.reserve.payment.domain.PaymentCommand;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "PaymentRequest.Payment", description = "결제 요청 객체")
    public static class Payment {

        @NotNull
        @Schema(description = "회원 ID", example = "1")
        private Long userId;

        @NotNull
        @Schema(description = "예약 ID", example = "1")
        private Long reservationId;

        @NotNull
        @Positive
        @Schema(description = "결제 금액", example = "50000")
        private int amount;

        public PaymentCommand.Payment toCommand(String token) {
            return PaymentCommand.Payment.builder()
                    .userId(userId)
                    .reservationId(reservationId)
                    .amount(amount)
                    .token(token)
                    .build();
        }
    }

}
