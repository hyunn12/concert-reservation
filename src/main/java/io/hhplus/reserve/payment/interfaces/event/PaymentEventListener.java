package io.hhplus.reserve.payment.interfaces.event;

import io.hhplus.reserve.common.kafka.KafkaConstant;
import io.hhplus.reserve.common.util.JsonUtil;
import io.hhplus.reserve.outbox.domain.Outbox;
import io.hhplus.reserve.outbox.domain.OutboxService;
import io.hhplus.reserve.payment.domain.event.PaymentSuccessEvent;
import io.hhplus.reserve.payment.infra.event.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final OutboxService outboxService;
    private final KafkaProducer kafkaProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOutbox(PaymentSuccessEvent event) {
        Outbox outbox = Outbox.create(
                "PAYMENT",
                KafkaConstant.PAYMENT_TOPIC,
                "PaymentSuccessEvent",
                JsonUtil.objectToJsonString(event)
        );
        outboxService.saveOutbox(outbox);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void paymentNotifyHandler(PaymentSuccessEvent event) {
        kafkaProducer.send(
                KafkaConstant.PAYMENT_TOPIC,
                event.getOutboxId(),
                event.getInfo()
        );
    }
}
