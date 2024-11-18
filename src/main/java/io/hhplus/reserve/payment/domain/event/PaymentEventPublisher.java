package io.hhplus.reserve.payment.domain.event;

public interface PaymentEventPublisher {

    void success(PaymentSuccessEvent event);

}
