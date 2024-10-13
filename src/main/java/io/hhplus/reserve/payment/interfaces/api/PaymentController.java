package io.hhplus.reserve.payment.interfaces.api;

import io.hhplus.reserve.payment.interfaces.dto.PaymentRequest;
import io.hhplus.reserve.payment.interfaces.dto.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/payment")
@Tag(name = "Payment", description = "결제 관련 API")
public class PaymentController {

    @Operation(summary = "결제", description = "예약한 콘서트 좌석 결제")
    @PostMapping("/payment")
    public PaymentResponse.Payment pay(
            @RequestBody PaymentRequest.Payment request
    ) {
        // TODO 결제 API 작성

        return PaymentResponse.Payment.builder()
                .userId(1L)
                .paymentAmount(request.getAmount())
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();
    }

}
