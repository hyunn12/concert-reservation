package io.hhplus.reserve.waiting.domain;

import io.hhplus.reserve.waiting.application.TokenCommand;
import io.hhplus.reserve.waiting.application.TokenInfo;
import org.springframework.stereotype.Service;

@Service
public class WaitingDomainService {
    // domainService
    // 여긴 로직이 있으면 안됨!!! 단순히 respository 설명 및 조합???????
    // Validator, Builder 같은 클래스는 따로 분리하는 게 맞다
    // Facade는 2개 이상의 서비스 호출할때만 사용한다
    // Controller 에서 바로 domain을 호출해도 된다
    // Facade 에서 사용하는 별도의 DTO
    // domain layer에서 domain service를 호출하기 위한 dto=Command

    // 활성 인원 수의 경우는???
    // 일단 하드코딩으로 넣구.. 실제론 외부 DB에 넣고?
    // application.yaml에 넣으면 재배포해야하니까 외부 저장소를 사용해야한다

    private final int ACTIVE_COUNT = 10;

    private final WaitingRepository waitingRepository;

    public WaitingDomainService(WaitingRepository waitingRepository) {
        this.waitingRepository = waitingRepository;
    }

    public TokenInfo.Token generateToken(TokenCommand.Generate command) {
        int activeCount = waitingRepository.getActiveCount(command.getConcertId());

        WaitingStatus status = activeCount < ACTIVE_COUNT ? WaitingStatus.ACTIVE : WaitingStatus.WAIT;

        Waiting waiting = Waiting.createToken(command.getUserId(), command.getConcertId(), status);

        Waiting savedWaiting = waitingRepository.createWaiting(waiting);

        return TokenInfo.Token.of(savedWaiting);
    }

    public TokenInfo.Status refreshToken(TokenCommand.Status command) {
        Waiting givenToken = waitingRepository.getWaiting(command.getToken());

        givenToken.validateToken();

        boolean isWaitingEmpty = waitingRepository.isWaitingEmpty(givenToken.getConcertId());

        WaitingStatus newStatus = isWaitingEmpty ? WaitingStatus.ACTIVE : WaitingStatus.WAIT;

        givenToken.refreshStatus(newStatus);

        int waitingCount = waitingRepository.getWaitingCount(givenToken.getConcertId());

        return TokenInfo.Status.of(givenToken, waitingCount);
    }

}
