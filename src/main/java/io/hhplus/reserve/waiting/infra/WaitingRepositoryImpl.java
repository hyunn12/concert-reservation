package io.hhplus.reserve.waiting.infra;

import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingRepository;
import jakarta.persistence.EntityNotFoundException;
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
    public int getWaitingCount(Waiting waiting) {
        return waitingJpaRepository.countWaitByConcertId(waiting.getConcertId(), waiting.getCreatedAt());
    }

    @Override
    public boolean isWaitingEmpty(Waiting waiting) {
        return waitingJpaRepository.countWaitByConcertId(waiting.getConcertId(), waiting.getCreatedAt()) == 0;
    }

    @Override
    public Waiting getWaiting(String token) {
        return waitingJpaRepository.findByToken(token).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Waiting saveWaiting(Waiting waiting) {
        return waitingJpaRepository.save(waiting);
    }
}
