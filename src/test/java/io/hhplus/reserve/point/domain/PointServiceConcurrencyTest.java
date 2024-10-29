package io.hhplus.reserve.point.domain;

import io.hhplus.reserve.TestContainerSupport;
import io.hhplus.reserve.point.infra.PointJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class PointServiceConcurrencyTest extends TestContainerSupport {

    // orm --
    @Autowired
    private PointJpaRepository pointJpaRepository;

    // sut --
    @Autowired
    private PointService pointService;

    private final Long pointId = 1L;
    private final Long userId = 1L;
    private final int initPoint = 10000;

    @BeforeEach
    void setUp() {
        Point point = new Point(pointId, userId, initPoint);
        pointJpaRepository.save(point);
    }

    @Test
    @DisplayName("한 유저가 동시에 포인트 사용하는 경우 순서대로 사용됨")
    void testUsePoint() throws InterruptedException {
        int threadCount = 10;
        int requestCount = 10;
        int usePoint = 2000;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(requestCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < requestCount; i++) {
            executorService.submit(() -> {
                try {
                    PointCommand.Action command = PointCommand.Action.builder()
                            .userId(userId)
                            .point(usePoint)
                            .build();
                    pointService.usePoint(command);

                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    log.error("[Exception] {}", e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        assertEquals(5, successCount.get());
        assertEquals(requestCount - successCount.get(), failCount.get());

        Point usedPoint = pointJpaRepository.findById(pointId).orElseThrow();
        assertNotNull(usedPoint.getUpdatedAt());
        assertEquals(0, usedPoint.getPoint());
    }

    @Test
    @DisplayName("한 유저가 동시에 포인트 충전하는 경우 순서대로 충전됨")
    void testChargePoint() throws InterruptedException {
        int threadCount = 10;
        int requestCount = 10;
        int chargePoint = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(requestCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < requestCount; i++) {
            executorService.submit(() -> {
                try {
                    PointCommand.Action command = PointCommand.Action.builder()
                            .userId(userId)
                            .point(chargePoint)
                            .build();
                    pointService.chargePoint(command);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    log.error("[Exception] {}", e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        assertEquals(10, successCount.get());
        assertEquals(0, failCount.get());

        Point chargedPoint = pointJpaRepository.findById(pointId).orElseThrow();
        assertNotNull(chargedPoint.getUpdatedAt());
        assertEquals(initPoint + (chargePoint * requestCount), chargedPoint.getPoint());
    }

}