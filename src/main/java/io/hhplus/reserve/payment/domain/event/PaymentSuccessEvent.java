package io.hhplus.reserve.payment.domain.event;

import io.hhplus.reserve.payment.domain.Payment;
import io.hhplus.reserve.payment.domain.PaymentInfo;
import io.hhplus.reserve.reservation.domain.Reservation;
import lombok.Getter;

@Getter
public class PaymentSuccessEvent {

    private final PaymentInfo.Main info;
    private final String token;

    public PaymentSuccessEvent(Payment payment, Reservation reservation, String token) {
        this.info = PaymentInfo.Main.of(payment, reservation);
        this.token = token;
    }
}
