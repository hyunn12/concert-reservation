package io.hhplus.reserve.waiting.domain;

import io.hhplus.reserve.waiting.application.TokenCommand;
import io.hhplus.reserve.waiting.application.TokenInfo;
import org.springframework.stereotype.Service;

@Service
public class WaitingDomainService {

    private final int ACTIVE_COUNT = 10;

    private final WaitingStore waitingStore;
    private final WaitingReader waitingReader;

    public WaitingDomainService(WaitingStore waitingStore, WaitingReader waitingReader) {
        this.waitingStore = waitingStore;
        this.waitingReader = waitingReader;
    }

    public TokenInfo.Token generateToken(TokenCommand.Generate command) {

        int activeCount = waitingReader.getActiveCount(command.getConcertId());

        WaitingStatus status = activeCount < ACTIVE_COUNT ? WaitingStatus.ACTIVE : WaitingStatus.WAIT;

        Waiting waiting = Waiting.createTokenBuilder()
                .userId(command.getUserId())
                .concertId(command.getConcertId())
                .status(status)
                .build();

        Waiting savedWaiting = waitingStore.createWaiting(waiting);

        return TokenInfo.Token.of(savedWaiting);
    }

    public TokenInfo.Status refreshToken(TokenCommand.Status command) {
        Waiting givenToken = waitingReader.getWaiting(command.getToken());

        boolean isWaitingEmpty = waitingReader.isWaitingEmpty(givenToken.getConcertId());

        WaitingStatus status = isWaitingEmpty ? WaitingStatus.ACTIVE : WaitingStatus.WAIT;

        Waiting waiting = Waiting.refreshTokenBuilder()
                .token(givenToken.getToken())
                .status(status)
                .build();

        int waitingCount = waitingReader.getWaitingCount(givenToken.getConcertId());

        return TokenInfo.Status.of(waiting, waitingCount);
    }

}
