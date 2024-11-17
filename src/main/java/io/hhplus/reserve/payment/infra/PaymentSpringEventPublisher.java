package io.hhplus.reserve.payment.infra;

import io.hhplus.reserve.payment.domain.event.PaymentEventPublisher;
import io.hhplus.reserve.payment.domain.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class PaymentSpringEventPublisher implements PaymentEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void success(PaymentSuccessEvent event) {
        log.info("PaymentEventPublisher::: {}", event);
        eventPublisher.publishEvent(event);
    }
}
