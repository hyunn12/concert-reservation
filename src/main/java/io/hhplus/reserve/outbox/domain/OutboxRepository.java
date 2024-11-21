package io.hhplus.reserve.outbox.domain;

import java.util.List;

public interface OutboxRepository {

    Outbox save(Outbox outbox);

    Outbox findById(String id);

    List<Outbox> findAllByIsPublished(boolean isPublished);
}
