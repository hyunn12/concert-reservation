package io.hhplus.reserve.waiting.domain;

import io.hhplus.reserve.waiting.application.TokenCommand;
import io.hhplus.reserve.waiting.application.TokenInfo;
import org.springframework.stereotype.Service;

@Service
public class WaitingDomainService {

    private final int ACTIVE_COUNT = 10;

    private final WaitingRepository waitingRepository;

    public WaitingDomainService(WaitingRepository waitingRepository) {
        this.waitingRepository = waitingRepository;
    }

    // 토큰 생성
    public TokenInfo.Token generateToken(TokenCommand.Generate command) {
        int activeCount = waitingRepository.getActiveCount(command.getConcertId());

        WaitingStatus status = activeCount < ACTIVE_COUNT ? WaitingStatus.ACTIVE : WaitingStatus.WAIT;

        Waiting waiting = Waiting.createToken(command.getUserId(), command.getConcertId(), status);

        Waiting savedWaiting = waitingRepository.createWaiting(waiting);

        return TokenInfo.Token.of(savedWaiting);
    }

    // 토큰 조회 및 갱신
    public TokenInfo.Status refreshToken(TokenCommand.Status command) {
        Waiting givenToken = validateToken(command.getToken());

        boolean isWaitingEmpty = waitingRepository.isWaitingEmpty(givenToken);

        WaitingStatus newStatus = isWaitingEmpty ? WaitingStatus.ACTIVE : WaitingStatus.WAIT;

        givenToken.refreshStatus(newStatus);

        int waitingCount = waitingRepository.getWaitingCount(givenToken);

        return TokenInfo.Status.of(givenToken, waitingCount);
    }

    // 토큰 검증
    public Waiting validateToken(String token) {
        Waiting givenToken = waitingRepository.getWaiting(token);
        givenToken.validateToken();
        return givenToken;
    }

}
