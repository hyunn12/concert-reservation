package io.hhplus.reserve.payment.interfaces.api;

import io.hhplus.reserve.common.CommonConstant;
import io.hhplus.reserve.payment.application.PaymentFacade;
import io.hhplus.reserve.payment.domain.PaymentInfo;
import io.hhplus.reserve.payment.interfaces.dto.PaymentRequest;
import io.hhplus.reserve.payment.interfaces.dto.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@Tag(name = "Payment", description = "결제 관련 API")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    @Operation(summary = "결제", description = "예약한 콘서트 좌석 결제")
    @PostMapping("/payment")
    public ResponseEntity<PaymentResponse.Payment> pay(
            @RequestHeader(CommonConstant.TOKEN) String token,
            @RequestBody PaymentRequest.Payment request
    ) {

        PaymentInfo.Main result = paymentFacade.pay(request.toCommand(token));

        return ResponseEntity.ok(PaymentResponse.Payment.of(result));
    }

}
