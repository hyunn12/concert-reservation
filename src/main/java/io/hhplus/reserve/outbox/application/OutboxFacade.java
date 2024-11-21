package io.hhplus.reserve.outbox.application;

import io.hhplus.reserve.common.annotation.Facade;
import io.hhplus.reserve.common.kafka.KafkaConstant;
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
            if (outbox.getCount() >= KafkaConstant.MAX_RETRY_COUNT) {
                outbox.unpublished();
                outboxService.saveOutbox(outbox);
                continue;
            }

            kafkaProducer.send(outbox.getTopic(), outbox.getId(), outbox.getMessage());
            outbox.increaseCount();
            outboxService.saveOutbox(outbox);
        }
    }

}
