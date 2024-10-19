package io.hhplus.reserve.waiting.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WaitingServiceIntegrationTest {

    @Autowired
    private WaitingService waitingService;

    @Autowired
    private WaitingRepository waitingRepository;

    @Test
    @DisplayName("토큰 생성 성공")
    void testGenerateToken() {
        TokenCommand.Generate command = TokenCommand.Generate.builder()
                .userId(1L)
                .concertId(1L)
                .build();

        TokenInfo.Token result = waitingService.generateToken(command);

        assertNotNull(result);
        assertEquals(WaitingStatus.ACTIVE.toString(), result.getStatus());
    }

    @Test
    @DisplayName("토큰 갱신")
    void testRefreshToken() {
        TokenCommand.Status command = TokenCommand.Status.builder()
                .token("testtokentokentoken")
                .build();

        Waiting savedWaiting = waitingRepository.saveWaiting(Waiting.createToken(1L, 1L, WaitingStatus.WAIT));

        TokenInfo.Status result = waitingService.refreshToken(command);

        assertNotNull(result);
        assertEquals(WaitingStatus.ACTIVE.toString(), result.getStatus());
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 갱신 시 예외 발생")
    void testRefreshInvalidToken() {
        TokenCommand.Status command = TokenCommand.Status.builder()
                .token("invalid_token")
                .build();

        // EntityNotFoundException 아닌 해당 예외가 발생하는 이유?
        assertThrows(JpaObjectRetrievalFailureException.class, () -> waitingService.refreshToken(command));
    }

    @Test
    @DisplayName("DELETE 상태 토큰으로 갱신 시 예외 발생")
    void testRefreshDeleteToken() {
        // given
        TokenCommand.Status command = TokenCommand.Status.builder()
                .token("deleted_token")
                .build();

        waitingRepository.saveWaiting(Waiting.createToken(1L, 1L, WaitingStatus.DELETE));

        // when & then
        assertThrows(IllegalStateException.class, () -> waitingService.refreshToken(command));
    }

    @Test
    @DisplayName("토큰 검증 성공")
    void testValidateToken() {
        // given
        String token = "testtokenuser2";
        waitingRepository.saveWaiting(Waiting.createToken(1L, 1L, WaitingStatus.ACTIVE));

        // when
        Waiting result = waitingService.validateToken(token);

        // then
        assertNotNull(result);
        assertEquals(token, result.getToken());
    }

}