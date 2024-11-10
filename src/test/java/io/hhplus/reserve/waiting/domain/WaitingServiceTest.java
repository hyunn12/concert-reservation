package io.hhplus.reserve.waiting.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static io.hhplus.reserve.waiting.domain.WaitingConstant.ACTIVE_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingServiceTest {

    @Mock
    private WaitingRepository waitingRepository;

    @InjectMocks
    private WaitingService waitingService;

    @Test
    @DisplayName("토큰값이 null 이면서 대기 인원이 적은 경우")
    void testCreateNewActiveToken() {

        when(waitingRepository.getActiveCount()).thenReturn(ACTIVE_SIZE - 1L);

        TokenInfo.Main result = waitingService.checkToken(null);

        verify(waitingRepository).getActiveCount();
        verify(waitingRepository).addActiveQueue(anyString());
        assertEquals(WaitingStatus.ACTIVE.toString(), result.getStatus());
    }

    @Test
    @DisplayName("토큰값이 null 이면서 대기 인원이 많은 경우")
    void testCreateNewWaitingToken() {
        when(waitingRepository.getActiveCount()).thenReturn(ACTIVE_SIZE + 1L);
        when(waitingRepository.getWaitingRank(anyString())).thenReturn(3L);

        TokenInfo.Main result = waitingService.checkToken(null);

        verify(waitingRepository).getActiveCount();
        verify(waitingRepository).addWaitingQueue(anyString());
        assertEquals(WaitingStatus.WAIT.toString(), result.getStatus());
    }

    @Test
    @DisplayName("대기열 토큰 상태 조회")
    void testCheckWaitingToken() {
        String token = UUID.randomUUID().toString();
        long rank = 5L;

        when(waitingRepository.getWaitingRank(token)).thenReturn(rank);

        TokenInfo.Main result = waitingService.checkToken(token);

        verify(waitingRepository).getWaitingRank(token);
        assertEquals(WaitingStatus.WAIT.toString(), result.getStatus());
        assertEquals(rank, result.getWaitingCount());
    }

    @Test
    @DisplayName("활성화 토큰 조회")
    void testCheckActiveToken() {
        String token = UUID.randomUUID().toString();
        when(waitingRepository.getWaitingRank(token)).thenReturn(0L);

        Waiting waiting = waitingService.getWaiting(token);

        assertEquals(WaitingStatus.ACTIVE, waiting.getStatus());
    }

    @Test
    @DisplayName("대기열 토큰 활성화")
    void testWaitingTokenToActive() {
        List<String> waitingTokens = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        when(waitingRepository.popWaitingTokens()).thenReturn(waitingTokens);

        waitingService.activeToken();

        waitingTokens.forEach(token -> verify(waitingRepository).addActiveQueue(token));
    }

    @Test
    @DisplayName("활성화 토큰 제거")
    void testRemoveActiveToken() {
        String token = UUID.randomUUID().toString();

        waitingService.removeActiveToken(token);

        verify(waitingRepository).removeActiveQueue(token);
    }

}