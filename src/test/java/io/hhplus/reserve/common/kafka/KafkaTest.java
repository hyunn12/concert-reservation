package io.hhplus.reserve.common.kafka;

import io.hhplus.reserve.config.TestContainerSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Slf4j
@ActiveProfiles("test")
class KafkaTest extends TestContainerSupport {

    @Autowired
    private KafkaProducerTest kafkaProducer;
    @Autowired
    private KafkaConsumerTest kafkaConsumer;

    @Test
    void kafkaTest() {
        int count = 10;
        String topic = "test-topic-1";

        for (int i = 0; i < count; i++) {
            String message = "Kafka Producer Message: " + i;
            log.info("message = {}", message);
            kafkaProducer.send(topic, message);
        }


        await().pollDelay(2, TimeUnit.SECONDS)
                .atMost(30, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(kafkaConsumer.getPayloadList())
                        .hasSize(count));
    }
}