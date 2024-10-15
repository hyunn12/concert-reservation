package io.hhplus.reserve.waiting.infra;

import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingRepository;
import org.springframework.stereotype.Repository;

@Repository
public class WaitingRepositoryImpl implements WaitingRepository {

    private final WaitingJpaRepository waitingJpaRepository;

    public WaitingRepositoryImpl(WaitingJpaRepository waitingJpaRepository) {
        this.waitingJpaRepository = waitingJpaRepository;
    }

    @Override
    public int getActiveCount(long concertId) {
        return waitingJpaRepository.countActiveByConcertId(concertId);
    }

    @Override
    public int getWaitingCount(long concertId) {
        return waitingJpaRepository.countWaitByConcertId(concertId);
    }

    @Override
    public boolean isWaitingEmpty(long concertId) {
        return waitingJpaRepository.countWaitByConcertId(concertId) == 0;
    }

    @Override
    public Waiting getWaiting(String token) {
        return waitingJpaRepository.findByToken(token);
    }

    @Override
    public Waiting createWaiting(Waiting waiting) {
        return waitingJpaRepository.save(waiting);
    }
}
