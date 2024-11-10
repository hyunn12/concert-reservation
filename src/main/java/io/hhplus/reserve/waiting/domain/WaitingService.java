package io.hhplus.reserve.waiting.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.hhplus.reserve.waiting.domain.WaitingConstant.*;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;

    // 대기열 상태 확인
    public TokenInfo.Main checkToken(String givenToken) {
        String token = givenToken;
        if (givenToken == null) {
            // 토큰 없을 경우 신규 생성
            token = UUID.randomUUID().toString();

            long activeCount = waitingRepository.getActiveCount();
            if (activeCount < ACTIVE_SIZE) {
                // 대기인원 적을 경우 active
                waitingRepository.addActiveQueue(token);
                Waiting waiting = Waiting.builder()
                        .token(token)
                        .status(WaitingStatus.ACTIVE)
                        .build();
                return TokenInfo.Main.of(waiting);
            }

            // waiting 대기열 추가
            waitingRepository.addWaitingQueue(token);
            return TokenInfo.Main.of(getWaiting(token));
        }

        // 토큰 존재 시 대기열 정보 조회
        return TokenInfo.Main.of(getWaiting(token));
    }

    // 대기열 조회
    public Waiting getWaiting(String token) {
        long waitingCount = waitingRepository.getWaitingRank(token);
        if (waitingCount <= 0) {
            // 대기인원 없을 경우 active
            return Waiting.builder()
                    .token(token)
                    .status(WaitingStatus.ACTIVE)
                    .build();
        }
        // 대기시간 계산 (스케줄러 단위 30s)
        long waitingTime = Math.max((long) Math.ceil((double) (waitingCount - 1) / ACTIVE_SIZE) * 30 * TimeUnit.SECONDS.toMillis(1), 0);
        return Waiting.builder()
                .token(token)
                .waitingCount(waitingCount)
                .waitingTime(waitingTime)
                .status(WaitingStatus.WAIT)
                .build();
    }

    // 활성토큰 여부 확인
    public boolean checkActiveToken(String token) {
        return waitingRepository.isActiveToken(token);
    }

    // 대기 -> 활성화
    public void activeToken() {
        List<String> waitingTokens = waitingRepository.popWaitingTokens();
        for (String token : waitingTokens) {
            waitingRepository.addActiveQueue(token);
        }
    }

    public void removeActiveToken(String token) {
        waitingRepository.removeActiveQueue(token);
    }

}
