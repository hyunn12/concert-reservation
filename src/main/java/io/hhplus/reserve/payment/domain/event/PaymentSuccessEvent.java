package io.hhplus.reserve.payment.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class PaymentSuccessEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long paymentId;
    private String token;
    private String outboxId;

    public static PaymentSuccessEvent create(Long paymentId, String token) {
        return new PaymentSuccessEvent(paymentId, token, UUID.randomUUID().toString());
    }
}
