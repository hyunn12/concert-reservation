package io.hhplus.reserve.outbox.application;

import io.hhplus.reserve.common.annotation.Facade;
import io.hhplus.reserve.outbox.domain.Outbox;
import io.hhplus.reserve.outbox.domain.OutboxService;
import io.hhplus.reserve.payment.infra.event.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Facade
@RequiredArgsConstructor
public class OutboxFacade {

    private final OutboxService outboxService;
    private final KafkaProducer kafkaProducer;

    public void retrySendOutboxMessage() {
        List<Outbox> outboxList = outboxService.getNotPublishedOutboxList();
        for (Outbox outbox : outboxList) {
            outboxService.increaseOutboxCount(outbox);
            kafkaProducer.send(outbox.getTopic(), outbox.getId(), outbox.getMessage());
        }
    }
}
