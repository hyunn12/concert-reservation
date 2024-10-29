package io.hhplus.reserve.reservation.application;

import io.hhplus.reserve.TestContainerSupport;
import io.hhplus.reserve.concert.domain.ConcertSeat;
import io.hhplus.reserve.concert.infra.ConcertSeatJpaRepository;
import io.hhplus.reserve.reservation.domain.ReserveCommand;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class ReserveFacadeConcurrencyTest extends TestContainerSupport {

    // orm --
    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;

    // sut --
    @Autowired
    private ReserveFacade reserveFacade;

    private Long seatId = 1L;
    private Long userId = 1L;

    @BeforeEach
    void setUp() {
        ConcertSeat seat = ConcertSeat.createBuilder().concertId(1L).seatNum(1).reservedAt(null).build();
        concertSeatJpaRepository.save(seat);
    }

    @Test
    @DisplayName("좌석 선점 동시성 테스트")
    void testSeatReservation() throws InterruptedException {
        int threadCount = 10;
        int requestCount = 50;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(requestCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < requestCount; i++) {
            executorService.submit(() -> {
                try {
                    ReserveCommand.Reserve command = ReserveCommand.Reserve.builder()
                            .userId(userId)
                            .seatIdList(List.of(seatId))
                            .build();
                    reserveFacade.reserve(command);
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

        assertEquals(1, successCount.get());
        assertEquals(requestCount - successCount.get(), failCount.get());
        ConcertSeat reservedSeat = concertSeatJpaRepository.findById(seatId).orElseThrow();
        assertNotNull(reservedSeat.getReservedAt());
    }
}