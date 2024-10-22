package io.hhplus.reserve.point.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void testUsePoint() {
        int threadCount = 10;
        int initPoint = 10000;
        int usePoint = 1000;
        Point point = Point.createBuilder().userId(userId).point(initPoint).build();

        when(pointRepository.getPointWithLock(userId)).thenReturn(point);

        final List<CompletableFuture<Void>> futures = IntStream.range(0, threadCount)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    PointCommand.Action command = PointCommand.Action.builder()
                            .userId(userId)
                            .point(usePoint)
                            .build();
                    pointService.usePoint(command);
                }))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        verify(pointRepository, times(threadCount)).getPointWithLock(userId);
        verify(pointRepository, times(threadCount)).savePoint(point);

        assertEquals(point.getPoint(), 0);
    }

    @Test
    @DisplayName("동시에 포인트 충전")
    void testChargePoint() {
        final int threadCount = 10;
        final int chargePoint = 1000;
        final Point point = Point.createBuilder().userId(userId).point(0).build();

        when(pointRepository.getPointWithLock(userId)).thenReturn(point);

        List<CompletableFuture<Void>> futures = IntStream.range(0, threadCount)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    PointCommand.Action command = PointCommand.Action.builder()
                            .userId(userId)
                            .point(chargePoint)
                            .build();
                    pointService.chargePoint(command);
                }))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        verify(pointRepository, times(threadCount)).getPointWithLock(userId);
        verify(pointRepository, times(threadCount)).savePoint(point);

        assertEquals(point.getPoint(), threadCount * chargePoint);
    }

}