package io.hhplus.reserve.payment.interfaces.api;

import io.hhplus.reserve.payment.interfaces.dto.PaymentRequest;
import io.hhplus.reserve.payment.interfaces.dto.PaymentResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @PostMapping("/payment")
    public PaymentResponse.Payment payment(@RequestBody PaymentRequest.Payment request) {
        // TODO 결제 API 작성

        return PaymentResponse.Payment.builder()
                .paymentId(1L)
                .userId(1L)
                .reservationId(1L)
                .paymentAmount(request.getAmount())
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();
    }

}
