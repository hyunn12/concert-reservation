package io.hhplus.reserve.payment.domain.event;

import io.hhplus.reserve.payment.domain.Payment;
import io.hhplus.reserve.payment.domain.PaymentInfo;
import io.hhplus.reserve.reservation.domain.Reservation;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PaymentSuccessEvent {

    private final PaymentInfo.Main info;
    private final String token;
    private final String outboxId;

    public PaymentSuccessEvent(Payment payment, Reservation reservation, String token, String outboxId) {
        this.info = PaymentInfo.Main.of(payment, reservation);
        this.token = token;
        this.outboxId = outboxId;
    }

    public static PaymentSuccessEvent create(Payment payment, Reservation reservation, String token) {
        return new PaymentSuccessEvent(payment, reservation, token, UUID.randomUUID().toString());
    }
}
