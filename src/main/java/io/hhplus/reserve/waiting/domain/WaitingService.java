package io.hhplus.reserve.waiting.domain;

import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.hhplus.reserve.waiting.domain.WaitingConstant.ACTIVE_KEY;
import static io.hhplus.reserve.waiting.domain.WaitingConstant.ACTIVE_LIMIT;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;

    public WaitingService(WaitingRepository waitingRepository) {
        this.waitingRepository = waitingRepository;
    }

    public TokenInfo.Token checkToken(String givenToken, Long concertId) {
        String token = givenToken;
        if (givenToken == null) {
            // 토큰 없을 경우 신규 생성
            token = UUID.randomUUID().toString();

            long activeCount = waitingRepository.getActiveCount(ACTIVE_KEY);
            if (activeCount < ACTIVE_LIMIT) {
                // 대기인원 적을 경우 active
                waitingRepository.addActiveQueue(token);
                Waiting waiting = Waiting.builder()
                        .token(token)
                        .concertId(concertId)
                        .build();
                return TokenInfo.Token.of(waiting);
            }

            // waiting 대기열 추가
            waitingRepository.addWaitingQueue(token, concertId);
            return TokenInfo.Token.of(getWaiting(token));
        }

        // 토큰 존재 시 대기열 정보 조회
        return TokenInfo.Token.of(getWaiting(token));
    }

    public Waiting getWaiting(String token) {
        long waitingCount = waitingRepository.getWaitingCount(token);
        if (waitingCount == 0) {
            // 대기인원 없을 경우 active
            return Waiting.builder()
                    .token(token)
                    .status(WaitingStatus.ACTIVE)
                    .build();
        }

        // 대기시간 계산
        long waitingTime = (long) Math.ceil((double) (waitingCount - 1) / ACTIVE_LIMIT) * 10;
        return Waiting.builder()
                .token(token)
                .waitingCount(waitingCount)
                .waitingTime(waitingTime)
                .status(WaitingStatus.WAIT)
                .build();
    }

}
