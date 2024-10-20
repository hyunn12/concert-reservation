package io.hhplus.reserve.payment.interfaces.dto;

import io.hhplus.reserve.payment.domain.PaymentInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentResponse {

    @Getter
    @Builder
    @Schema(name = "PaymentResponse.Payment", description = "결제 결과 객체")
    public static class Payment {

        @Schema(description = "회원 ID", example = "1")
        private Long userId;

        @Schema(description = "결제금액", example = "50000")
        private int paymentAmount;

        @Schema(description = "결제상태", example = "SUCCESS")
        private String status;

        @Schema(description = "생성일", example = "2024-10-13 12:00:00")
        private LocalDateTime createdAt;

        public static Payment of(PaymentInfo.Main info) {
            return Payment.builder()
                    .userId(info.getUserId())
                    .paymentAmount(info.getPaymentAmount())
                    .status(info.getStatus())
                    .createdAt(info.getCreateAt())
                    .build();
        }
    }

}
