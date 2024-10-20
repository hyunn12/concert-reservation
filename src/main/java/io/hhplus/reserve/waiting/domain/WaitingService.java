package io.hhplus.reserve.waiting.domain;

import org.springframework.stereotype.Service;

@Service
public class WaitingService {

    private final int ACTIVE_COUNT = 10;

    private final WaitingRepository waitingRepository;

    public WaitingService(WaitingRepository waitingRepository) {
        this.waitingRepository = waitingRepository;
    }

    // 토큰 생성
    public TokenInfo.Token generateToken(TokenCommand.Generate command) {
        int activeCount = waitingRepository.getActiveCount(command.getConcertId());

        WaitingStatus status = activeCount < ACTIVE_COUNT ? WaitingStatus.ACTIVE : WaitingStatus.WAIT;

        Waiting waiting = Waiting.createToken(command.getUserId(), command.getConcertId(), status);

        Waiting savedWaiting = waitingRepository.saveWaiting(waiting);

        return TokenInfo.Token.of(savedWaiting);
    }

    // 토큰 조회 및 갱신
    public TokenInfo.Status refreshToken(TokenCommand.Status command) {
        Waiting givenToken = validateToken(command.getToken());

        int waitingCount = waitingRepository.getWaitingCount(givenToken);
        givenToken.activateStatusNoWaiting(waitingCount);

        waitingRepository.saveWaiting(givenToken);

        return TokenInfo.Status.of(givenToken, waitingCount);
    }

    // 토큰 검증
    public Waiting validateToken(String token) {
        Waiting givenToken = waitingRepository.getWaiting(token);
        givenToken.validateToken();
        return givenToken;
    }

    // 토큰 삭제
    public void deleteToken(Waiting waiting) {
        waiting.deleteToken();
        waitingRepository.saveWaiting(waiting);
    }

}
