package io.hhplus.reserve.point.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceConcurrencyTest {

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private PointService pointService;

    private final Long userId = 1L;

    @Test
    @DisplayName("동시에 포인트 사용")
    void testUsePoint() throws InterruptedException {

        int threadCount = 10;
        int requestCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Point point = mock(Point.class);
        when(pointRepository.getPointWithLock(userId)).thenReturn(point);

        doNothing().when(point).usePoint(anyInt());

        for (int i = 0; i < requestCount; i++) {
            executorService.submit(() -> {
                try {
                    PointCommand.Action command = PointCommand.Action.builder().userId(userId).point(1000).build();
                    pointService.usePoint(command);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        verify(pointRepository, times(requestCount)).getPointWithLock(userId);
        verify(point, times(requestCount)).usePoint(anyInt());
    }

    @Test
    @DisplayName("동시에 포인트 충전")
    void testChargePoint() {

    }

}