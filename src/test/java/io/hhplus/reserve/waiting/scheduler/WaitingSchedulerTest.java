package io.hhplus.reserve.waiting.scheduler;

import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingSchedulerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingSchedulerTest {

    @Mock
    private WaitingSchedulerService schedulerService;

    @InjectMocks
    private WaitingScheduler waitingScheduler;

    @Test
    @DisplayName("만료된 대기열 삭제 스케줄러 정상작동")
    void testDeleteExpiredWaiting() {
        Waiting waiting1 = mock(Waiting.class);
        Waiting waiting2 = mock(Waiting.class);

        when(schedulerService.getExpiredWaitingList()).thenReturn(List.of(waiting1, waiting2));

        waitingScheduler.deleteExpiredWaiting();

        verify(schedulerService).getExpiredWaitingList();
        verify(waiting1).deleteToken();
        verify(waiting2).deleteToken();
        verify(schedulerService).saveAll(anyList());
    }

}