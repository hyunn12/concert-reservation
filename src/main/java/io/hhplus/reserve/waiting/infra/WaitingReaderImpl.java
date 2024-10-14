package io.hhplus.reserve.waiting.infra;

import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WaitingReaderImpl implements WaitingReader {

    private final WaitingJpaRepository waitingJpaRepository;

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


}
