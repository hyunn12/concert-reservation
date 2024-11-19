package io.hhplus.reserve.common.kafka;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Component
public class KafkaConsumerTest {

    private final List<String> payloadList = new ArrayList<>();

    @KafkaListener(topics = "test-topic-1", groupId = "test-group-1")
    public void consume(String payload) {
        log.debug("### KafkaConsumerTest ::: payload: {}", payload);
        payloadList.add(payload);
    }
}
