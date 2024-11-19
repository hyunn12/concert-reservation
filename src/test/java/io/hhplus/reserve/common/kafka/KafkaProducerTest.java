package io.hhplus.reserve.common.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerTest {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String message) {
        log.debug("### KafkaProducerTest ::: topic: {}, message: {}", topic, message);
        kafkaTemplate.send(topic, message);
    }
}
