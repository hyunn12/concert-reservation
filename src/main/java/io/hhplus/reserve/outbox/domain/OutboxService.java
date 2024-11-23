package io.hhplus.reserve.outbox.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;

    public void saveOutbox(Outbox outbox) {
        outboxRepository.save(outbox);
    }

    @Transactional
    public void publishOutbox(String id) {
        Outbox outbox = outboxRepository.findById(id);
        outbox.published();
        outboxRepository.save(outbox);
    }

    @Transactional
    public void increaseOutboxCount(Outbox outbox) {
        outbox.increaseCount();
        outboxRepository.save(outbox);
    }

    public List<Outbox> getNotPublishedOutboxList() {
        return outboxRepository.findAllByIsPublished(false);
    }
}
