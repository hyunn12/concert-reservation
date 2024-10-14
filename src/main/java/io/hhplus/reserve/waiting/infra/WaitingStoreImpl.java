package io.hhplus.reserve.waiting.infra;

import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WaitingStoreImpl implements WaitingStore {

    private final WaitingJpaRepository waitingJpaRepository;

    @Override
    public Waiting createWaiting(Waiting waiting) {
        return waitingJpaRepository.save(waiting);
    }

}
