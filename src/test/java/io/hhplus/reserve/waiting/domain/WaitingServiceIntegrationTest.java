package io.hhplus.reserve.waiting.domain;

import io.hhplus.reserve.TestContainerSupport;
import io.hhplus.reserve.support.domain.exception.BusinessException;
import io.hhplus.reserve.waiting.infra.WaitingJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class WaitingServiceIntegrationTest extends TestContainerSupport {

    // orm --
    @Autowired
    private WaitingJpaRepository waitingJpaRepository;

    // sut --
    @Autowired
    private WaitingService waitingService;

    private Waiting waiting;
    private Waiting deletedWaiting;
    private String validToken;
    private String deletedToken;

    @BeforeEach
    void setUp() {
        waiting = new Waiting(1L, 1L, WaitingStatus.WAIT);
        deletedWaiting = new Waiting(2L, 1L, WaitingStatus.DELETE);
        waitingJpaRepository.saveAll(List.of(waiting, deletedWaiting));
        validToken = waiting.getToken();
        deletedToken = deletedWaiting.getToken();
    }

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
                .token(validToken)
                .build();

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
                .token(deletedToken)
                .build();

        // when & then
        assertThrows(BusinessException.class, () -> waitingService.refreshToken(command));
    }

    @Test
    @DisplayName("토큰 검증 성공")
    void testValidateToken() {
        // when
        Waiting result = waitingService.validateToken(validToken);

        // then
        assertNotNull(result);
        assertEquals(validToken, result.getToken());
    }

}