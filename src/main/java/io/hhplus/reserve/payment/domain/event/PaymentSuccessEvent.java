package io.hhplus.reserve.payment.domain.event;

import io.hhplus.reserve.payment.domain.Payment;
import io.hhplus.reserve.payment.domain.PaymentInfo;
import io.hhplus.reserve.reservation.domain.Reservation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PaymentSuccessEvent extends ApplicationEvent {

    private final PaymentInfo.Main info;
    private final String token;

    public PaymentSuccessEvent(Object source, Payment payment, Reservation reservation, String token) {
        super(source);
        this.info = PaymentInfo.Main.of(payment, reservation);
        this.token = token;
    }
}
