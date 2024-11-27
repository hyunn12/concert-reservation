package io.hhplus.reserve.outbox.infra;

import io.hhplus.reserve.common.kafka.KafkaConstant;
import io.hhplus.reserve.outbox.domain.Outbox;
import io.hhplus.reserve.outbox.domain.OutboxRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OutboxRepositoryImpl implements OutboxRepository {

    private final OutboxJpaRepository outboxJpaRepository;

    @Override
    public Outbox save(Outbox outbox) {
        return outboxJpaRepository.save(outbox);
    }

    @Override
    public Outbox findById(String id) {
        return outboxJpaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Outbox> findAllByIsPublished(boolean isPublished) {
        return outboxJpaRepository.findAllByPublished(isPublished, KafkaConstant.MAX_RETRY_COUNT);
    }
}
