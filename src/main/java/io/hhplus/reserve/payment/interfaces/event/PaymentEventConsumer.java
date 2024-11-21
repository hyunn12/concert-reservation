package io.hhplus.reserve.payment.interfaces.event;

import io.hhplus.reserve.common.kafka.KafkaConstant;
import io.hhplus.reserve.common.util.JsonUtil;
import io.hhplus.reserve.external.application.ExternalService;
import io.hhplus.reserve.outbox.domain.Outbox;
import io.hhplus.reserve.outbox.domain.OutboxService;
import io.hhplus.reserve.payment.domain.PaymentInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final OutboxService outboxService;
    private final ExternalService externalService;

    @KafkaListener(topics = KafkaConstant.PAYMENT_TOPIC, groupId = "payment-outbox")
    public void outboxPublished(ConsumerRecord<String, String> consumerRecord){
        log.info("# [PaymentEventConsumer] outboxPublished ::: {}", consumerRecord.key());
        Outbox outbox = outboxService.getOutboxById(consumerRecord.key());
        outbox.published();
        outboxService.saveOutbox(outbox);
    }

    @KafkaListener(topics = KafkaConstant.PAYMENT_TOPIC, groupId = "payment-notify")
    public void successPayment(ConsumerRecord<String, String> consumerRecord){
        log.info("# [PaymentEventConsumer] successPayment ::: {}", consumerRecord.value());
        PaymentInfo.Main info = JsonUtil.jsonStringToObject(consumerRecord.value(), PaymentInfo.Main.class);
        externalService.notifyPaymentSuccess(info);
    }
}
