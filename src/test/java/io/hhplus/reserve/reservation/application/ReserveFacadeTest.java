package io.hhplus.reserve.reservation.application;

import io.hhplus.reserve.TestContainerSupport;
import io.hhplus.reserve.concert.domain.Concert;
import io.hhplus.reserve.concert.domain.ConcertSeat;
import io.hhplus.reserve.concert.domain.SeatStatus;
import io.hhplus.reserve.concert.infra.ConcertJpaRepository;
import io.hhplus.reserve.concert.infra.ConcertSeatJpaRepository;
import io.hhplus.reserve.reservation.domain.ReserveCommand;
import io.hhplus.reserve.reservation.domain.ReserveInfo;
import io.hhplus.reserve.support.domain.exception.BusinessException;
import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.infra.WaitingJpaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReserveFacadeTest extends TestContainerSupport {

    // orm --
    @Autowired
    private WaitingJpaRepository waitingJpaRepository;
    @Autowired
    private ConcertJpaRepository concertJpaRepository;
    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;

    // sut --
    @Autowired
    private ReserveFacade reserveFacade;

    @BeforeEach
    void setUp() {
        Waiting waiting1 = new Waiting(1L, 1L, 1L, "valid_token", null);
        Waiting waiting2 = new Waiting(2L, 2L, 1L, "expired_token", null);
        waitingJpaRepository.saveAll(List.of(waiting1, waiting2));

        Concert concert = new Concert(1L,
                "AA Concert",
                "AA concert desc",
                LocalDateTime.of(2024, 12, 25, 12, 0),
                LocalDateTime.of(2024, 12, 25, 16, 0),
                LocalDateTime.of(2024, 9, 21, 0, 0),
                LocalDateTime.of(2024, 11, 23, 23, 59));
        concertJpaRepository.save(concert);

        ConcertSeat seat1 = new ConcertSeat(1L, 1L, 1, SeatStatus.AVAILABLE, null, 0L);
        ConcertSeat seat2 = new ConcertSeat(2L, 1L, 2, SeatStatus.AVAILABLE, null, 0L);
        ConcertSeat seat3 = new ConcertSeat(3L, 1L, 3, SeatStatus.AVAILABLE, LocalDateTime.now().minusMinutes(3), 0L);
        ConcertSeat seat4 = new ConcertSeat(4L, 1L, 4, SeatStatus.CONFIRMED, LocalDateTime.now().minusMinutes(10), 0L);
        concertSeatJpaRepository.saveAll(List.of(seat1, seat2, seat3, seat4));

    }

    @Nested
    @DisplayName("낙관적락을 이용한 예약")
    class OptimisticLock {

        @Test
        @DisplayName("유효한 좌석에 대해 예약 성공")
        @Transactional
        void testReserveSuccess() {
            // given
            Long userId = 1L;
            List<Long> seatIdList = List.of(1L, 2L);

            ReserveCommand.Reserve command = ReserveCommand.Reserve.builder()
                    .userId(userId)
                    .seatIdList(seatIdList)
                    .build();

            // when
            ReserveInfo.Reserve result = reserveFacade.reserve(command);

            // then
            assertNotNull(result);
            assertEquals(userId, result.getUserId());
            assertEquals(seatIdList, result.getSeatIdList());
        }

        @Test
        @DisplayName("이미 예약된 좌석일 시 예외 발생")
        @Transactional
        void testSeatAlreadyReserved() {
            // given
            Long userId = 1L;
            List<Long> seatIdList = List.of(1L, 3L);

            ReserveCommand.Reserve command = ReserveCommand.Reserve.builder()
                    .userId(userId)
                    .seatIdList(seatIdList)
                    .build();

            // when / then
            assertThrows(BusinessException.class, () -> reserveFacade.reserve(command));
        }

        @Test
        @DisplayName("이미 확정된 좌석일 시 예외 발생")
        @Transactional
        void testSeatAlreadyConfirmed() {
            // given
            Long userId = 1L;
            List<Long> seatIdList = List.of(1L, 4L);

            ReserveCommand.Reserve command = ReserveCommand.Reserve.builder()
                    .userId(userId)
                    .seatIdList(seatIdList)
                    .build();

            // when / then
            assertThrows(BusinessException.class, () -> reserveFacade.reserve(command));
        }

    }

    @Nested
    @DisplayName("Redis 사용한 분산락을 이용한 예약")
    class RedisLock {

        @Test
        @DisplayName("유효한 좌석에 대해 예약 성공")
        void testReserveSuccess() {
            // given
            Long userId = 1L;
            Long concertId = 1L;
            List<Long> seatIdList = List.of(1L);

            ReserveCommand.Reserve command = ReserveCommand.Reserve.builder()
                    .userId(userId)
                    .concertId(concertId)
                    .seatIdList(seatIdList)
                    .build();

            // when
            ReserveInfo.Reserve result = reserveFacade.reserveWithRedis(command);

            // then
            assertNotNull(result);
            assertEquals(userId, result.getUserId());
            assertEquals(seatIdList, result.getSeatIdList());
        }

        @Test
        @DisplayName("이미 예약된 좌석일 시 예외 발생")
        void testSeatAlreadyReserved() {
            // given
            Long userId = 1L;
            List<Long> seatIdList = List.of(1L, 3L);

            ReserveCommand.Reserve command = ReserveCommand.Reserve.builder()
                    .userId(userId)
                    .seatIdList(seatIdList)
                    .build();

            // when / then
            assertThrows(BusinessException.class, () -> reserveFacade.reserveWithRedis(command));
        }

        @Test
        @DisplayName("이미 확정된 좌석일 시 예외 발생")
        void testSeatAlreadyConfirmed() {
            // given
            Long userId = 1L;
            List<Long> seatIdList = List.of(1L, 4L);

            ReserveCommand.Reserve command = ReserveCommand.Reserve.builder()
                    .userId(userId)
                    .seatIdList(seatIdList)
                    .build();

            // when / then
            assertThrows(BusinessException.class, () -> reserveFacade.reserveWithRedis(command));
        }
    }
}