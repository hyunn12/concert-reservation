package io.hhplus.reserve.outbox.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;

    public void saveOutbox(Outbox outbox) {
        outboxRepository.save(outbox);
    }

    public Outbox getOutboxById(String id) {
        return outboxRepository.findById(id);
    }
}
