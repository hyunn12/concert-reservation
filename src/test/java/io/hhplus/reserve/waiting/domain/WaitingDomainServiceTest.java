package io.hhplus.reserve.waiting.domain;

import io.hhplus.reserve.waiting.application.TokenCommand;
import io.hhplus.reserve.waiting.application.TokenInfo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingDomainServiceTest {

    @Mock
    private WaitingRepository waitingRepository;

    @InjectMocks
    private WaitingDomainService waitingDomainService;

    @Nested
    @DisplayName("토큰 생성")
    class GenerateToken {

        @Test
        @DisplayName("ACTIVE_COUNT 보다 활성인원 수가 적은 경우 ACTIVE 토큰 반환")
        void generateActiveToken() {
            // given
            TokenCommand.Generate command = TokenCommand.Generate.builder().userId(1L).concertId(1L).build();
            given(waitingRepository.getActiveCount(command.getConcertId())).willReturn(5);

            Waiting waiting = Waiting.createToken(command.getUserId(), command.getConcertId(), WaitingStatus.ACTIVE);
            given(waitingRepository.createWaiting(any(Waiting.class))).willReturn(waiting);

            // when
            TokenInfo.Token result = waitingDomainService.generateToken(command);

            // then
            assertEquals(result.getStatus(), WaitingStatus.ACTIVE.toString());
            then(waitingRepository).should(times(1)).getActiveCount(command.getConcertId());
            then(waitingRepository).should(times(1)).createWaiting(any(Waiting.class));
        }

        @Test
        @DisplayName("활성인원 수가 많은 경우 WAIT 토큰 반환")
        void generateWaitToken() {
            // given
            TokenCommand.Generate command = TokenCommand.Generate.builder().userId(1L).concertId(1L).build();
            given(waitingRepository.getActiveCount(command.getConcertId())).willReturn(10);

            Waiting waiting = Waiting.createToken(command.getUserId(), command.getConcertId(), WaitingStatus.WAIT);
            given(waitingRepository.createWaiting(any(Waiting.class))).willReturn(waiting);

            // when
            TokenInfo.Token result = waitingDomainService.generateToken(command);

            // then
            assertEquals(result.getStatus(), WaitingStatus.WAIT.toString());
            then(waitingRepository).should(times(1)).getActiveCount(command.getConcertId());
            then(waitingRepository).should(times(1)).createWaiting(any(Waiting.class));
        }

    }

    @Nested
    @DisplayName("토큰 조회 및 활성화")
    class RefreshToken {

        @Test
        @DisplayName("대기인원 없을 경우 토큰 상태 활성화 ACTIVE")
        void refreshTokenWaitToActive() {
            // given
            TokenCommand.Status command = TokenCommand.Status.builder().token("testtoken_123123").build();
            Waiting givenToken = new Waiting(1L, 1L, 1L, command.getToken(), WaitingStatus.WAIT);

            given(waitingRepository.getWaiting(command.getToken())).willReturn(givenToken);
            given(waitingRepository.isWaitingEmpty(givenToken.getConcertId())).willReturn(true);
            given(waitingRepository.getWaitingCount(givenToken.getConcertId())).willReturn(0);

            // when
            TokenInfo.Status result = waitingDomainService.refreshToken(command);

            // then
            assertEquals(result.getStatus(), WaitingStatus.ACTIVE.toString());
            then(waitingRepository).should(times(1)).isWaitingEmpty(givenToken.getConcertId());
            then(waitingRepository).should(times(1)).getWaitingCount(givenToken.getConcertId());
        }

        @Test
        @DisplayName("대기인원 있을 경우 토큰 상태 유지 WAIT")
        void refreshToken() {
            // given
            TokenCommand.Status command = TokenCommand.Status.builder().token("testtoken_123123").build();
            Waiting givenToken = new Waiting(1L, 1L, 1L, command.getToken(), WaitingStatus.WAIT);

            given(waitingRepository.getWaiting(command.getToken())).willReturn(givenToken);
            given(waitingRepository.isWaitingEmpty(givenToken.getConcertId())).willReturn(false);
            given(waitingRepository.getWaitingCount(givenToken.getConcertId())).willReturn(10);

            // when
            TokenInfo.Status result = waitingDomainService.refreshToken(command);

            // then
            assertEquals(result.getStatus(), WaitingStatus.WAIT.toString());
            then(waitingRepository).should(times(1)).isWaitingEmpty(givenToken.getConcertId());
            then(waitingRepository).should(times(1)).getWaitingCount(givenToken.getConcertId());
        }

        @Test
        @DisplayName("존재하지 않는 토큰으로 조회 시 예외 발생")
        void shouldThrowExceptionWhenTokenNotFound() {
            // given
            TokenCommand.Status command = TokenCommand.Status.builder().token("invalid_token").build();

            given(waitingRepository.getWaiting(command.getToken())).willThrow(new EntityNotFoundException());

            // when / then
            assertThrows(EntityNotFoundException.class, () -> {
                waitingDomainService.refreshToken(command);
            });

            then(waitingRepository).should(times(1)).getWaiting(command.getToken());
            then(waitingRepository).should(never()).isWaitingEmpty(any(Long.class));
            then(waitingRepository).should(never()).getWaitingCount(any(Long.class));
        }

        @Test
        @DisplayName("유효하지 않은 토큰으로 조회 시 예외 발생")
        void shouldThrowExceptionWhenTokenIsDeleted() {
            // given
            TokenCommand.Status command = TokenCommand.Status.builder().token("testtokentokentoken").build();
            Waiting givenToken = new Waiting(1L, 1L, 1L, command.getToken(), WaitingStatus.DELETE);

            given(waitingRepository.getWaiting(command.getToken())).willReturn(givenToken);

            // when / then
            assertThrows(IllegalStateException.class, () -> {
                waitingDomainService.refreshToken(command);
            });

            then(waitingRepository).should(times(1)).getWaiting(command.getToken());
            then(waitingRepository).should(never()).isWaitingEmpty(any(Long.class));
            then(waitingRepository).should(never()).getWaitingCount(any(Long.class));
        }

    }

}