package io.hhplus.reserve.outbox.domain;

public interface OutboxRepository {

    Outbox save(Outbox outbox);

    Outbox findById(String id);
}
