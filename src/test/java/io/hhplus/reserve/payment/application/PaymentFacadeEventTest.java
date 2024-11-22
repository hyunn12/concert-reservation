package io.hhplus.reserve.payment.application;

import io.hhplus.reserve.common.kafka.KafkaConstant;
import io.hhplus.reserve.common.util.JsonUtil;
import io.hhplus.reserve.config.TestContainerSupport;
import io.hhplus.reserve.outbox.domain.Outbox;
import io.hhplus.reserve.outbox.domain.OutboxRepository;
import io.hhplus.reserve.payment.domain.event.PaymentSuccessEvent;
import io.hhplus.reserve.payment.infra.event.KafkaProducer;
import io.hhplus.reserve.waiting.domain.WaitingRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Slf4j
class PaymentFacadeEventTest extends TestContainerSupport {

    @Autowired
    private WaitingRepository waitingRepository;

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    void testJsonSerialization() {
        PaymentSuccessEvent event = new PaymentSuccessEvent(1L, "token123", "outbox123");
        String json = JsonUtil.objectToJsonString(event);
        assertThat(json).isNotEmpty();

        PaymentSuccessEvent deserializedEvent = JsonUtil.jsonStringToObject(json, PaymentSuccessEvent.class);
        assertThat(deserializedEvent.getPaymentId()).isEqualTo(1L);
        assertThat(deserializedEvent.getToken()).isEqualTo("token123");
    }

    @Test
    @DisplayName("결제 성공 메시지 발행 테스트")
    void consumeTest() {
        String token = UUID.randomUUID().toString();

        PaymentSuccessEvent event = PaymentSuccessEvent.create(1L, token);

        Outbox outbox = Outbox.create(
                event.getOutboxId(),
                "PAYMENT",
                KafkaConstant.PAYMENT_TOPIC,
                "PaymentSuccessEvent",
                JsonUtil.objectToJsonString(event)
        );
        outboxRepository.save(outbox);

        kafkaProducer.send(
                KafkaConstant.PAYMENT_TOPIC,
                event.getOutboxId(),
                event.getPaymentId()
        );

        // then
        await().pollDelay(2, TimeUnit.SECONDS)
                .atMost(30, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Outbox updatedOutbox = outboxRepository.findById(event.getOutboxId());
                    assertThat(updatedOutbox.isPublished()).isEqualTo(true);
                });
    }
}