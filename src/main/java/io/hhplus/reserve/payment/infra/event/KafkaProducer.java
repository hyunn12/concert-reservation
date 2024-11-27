package io.hhplus.reserve.payment.infra.event;

import io.hhplus.reserve.common.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String id, Object payload) {
        kafkaTemplate.send(topic, id, JsonUtil.objectToJsonString(payload)).whenComplete((result, exception) -> {
            if (exception == null) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("# [KafkaProducer] ::: topic: {}, value: {}, offset: {}", metadata.topic(), result.getProducerRecord().value(), metadata.offset());
            } else {
                log.error("# [KafkaProducer] ::: {}", exception.getMessage());
                throw new RuntimeException("Kafka Producer Send Message Fail", exception);
            }
        });
    }
}
