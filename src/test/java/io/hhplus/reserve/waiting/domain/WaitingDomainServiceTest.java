package io.hhplus.reserve.waiting.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingDomainServiceTest {

    @Mock
    private WaitingStore waitingStore;
    @Mock
    private WaitingReader waitingReader;

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
            given(waitingReader.getActiveCount(command.getConcertId())).willReturn(5);

            Waiting waiting = Waiting.createTokenBuilder()
                    .userId(command.getUserId())
                    .concertId(command.getConcertId())
                    .status(WaitingStatus.ACTIVE)
                    .build();
            given(waitingStore.createWaiting(any(Waiting.class))).willReturn(waiting);

            // when
            TokenInfo.Token result = waitingDomainService.generateToken(command);

            // then
            assertThat(result.getStatus()).isEqualTo(WaitingStatus.ACTIVE.toString());
            then(waitingReader).should(times(1)).getActiveCount(command.getConcertId());
            then(waitingStore).should(times(1)).createWaiting(any(Waiting.class));
        }

        @Test
        @DisplayName("활성인원 수가 많은 경우 WAIT 토큰 반환")
        void generateWaitToken() {
            // given
            TokenCommand.Generate command = TokenCommand.Generate.builder().userId(1L).concertId(1L).build();
            given(waitingReader.getActiveCount(command.getConcertId())).willReturn(10);

            Waiting waiting = Waiting.createTokenBuilder()
                    .userId(command.getUserId())
                    .concertId(command.getConcertId())
                    .status(WaitingStatus.WAIT)
                    .build();
            given(waitingStore.createWaiting(any(Waiting.class))).willReturn(waiting);

            // when
            TokenInfo.Token result = waitingDomainService.generateToken(command);

            // then
            assertThat(result.getStatus()).isEqualTo(WaitingStatus.WAIT.toString());
            then(waitingReader).should(times(1)).getActiveCount(command.getConcertId());
            then(waitingStore).should(times(1)).createWaiting(any(Waiting.class));
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

            given(waitingReader.getWaiting(command.getToken())).willReturn(givenToken);
            given(waitingReader.isWaitingEmpty(givenToken.getConcertId())).willReturn(true);
            given(waitingReader.getWaitingCount(givenToken.getConcertId())).willReturn(0);

            // when
            TokenInfo.Status result = waitingDomainService.refreshToken(command);

            // then
            assertThat(result.getStatus()).isEqualTo(WaitingStatus.ACTIVE.toString());
            then(waitingReader).should(times(1)).isWaitingEmpty(givenToken.getConcertId());
            then(waitingReader).should(times(1)).getWaitingCount(givenToken.getConcertId());
        }

        @Test
        @DisplayName("대기인원 있을 경우 토큰 상태 유지 WAIT")
        void refreshToken() {
            // given
            TokenCommand.Status command = TokenCommand.Status.builder().token("testtoken_123123").build();
            Waiting givenToken = new Waiting(1L, 1L, 1L, command.getToken(), WaitingStatus.WAIT);

            given(waitingReader.getWaiting(command.getToken())).willReturn(givenToken);
            given(waitingReader.isWaitingEmpty(givenToken.getConcertId())).willReturn(false);
            given(waitingReader.getWaitingCount(givenToken.getConcertId())).willReturn(10);

            // when
            TokenInfo.Status result = waitingDomainService.refreshToken(command);

            // then
            assertThat(result.getStatus()).isEqualTo(WaitingStatus.WAIT.toString());
            then(waitingReader).should(times(1)).isWaitingEmpty(givenToken.getConcertId());
            then(waitingReader).should(times(1)).getWaitingCount(givenToken.getConcertId());
        }

    }

}