package io.hhplus.reserve.reservation.application;

import io.hhplus.reserve.concert.domain.ConcertSeat;
import io.hhplus.reserve.concert.domain.ConcertService;
import io.hhplus.reserve.concert.domain.SeatStatus;
import io.hhplus.reserve.concert.infra.ConcertSeatJpaRepository;
import io.hhplus.reserve.reservation.domain.ReserveCommand;
import io.hhplus.reserve.reservation.domain.ReserveInfo;
import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingService;
import io.hhplus.reserve.waiting.infra.WaitingJpaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class ReserveFacadeTest {

    @Autowired
    private ConcertService concertService;
    @Autowired
    private WaitingService waitingService;
    @Autowired
    private WaitingJpaRepository waitingJpaRepository;
    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;

    private ReserveFacade reserveFacade;

    @BeforeEach
    void setUp() {
        reserveFacade = new ReserveFacade(concertService, waitingService);

        concertSeatJpaRepository.deleteAll();
        waitingJpaRepository.deleteAll();

        Waiting waiting1 = new Waiting(1L, 1L, 1L, "valid_token", null);
        Waiting waiting2 = new Waiting(2L, 2L, 1L, "expired_token", null);
        waitingJpaRepository.save(waiting1);
        waitingJpaRepository.save(waiting2);

        ConcertSeat seat1 = new ConcertSeat(1L, 1L, 1, SeatStatus.AVAILABLE, null);
        ConcertSeat seat2 = new ConcertSeat(2L, 1L, 2, SeatStatus.AVAILABLE, null);
        ConcertSeat seat3 = new ConcertSeat(3L, 1L, 3, SeatStatus.AVAILABLE, LocalDateTime.now().minusMinutes(3));
        ConcertSeat seat4 = new ConcertSeat(4L, 1L, 4, SeatStatus.CONFIRMED, LocalDateTime.now().minusMinutes(10));
        concertSeatJpaRepository.saveAll(List.of(seat1, seat2, seat3, seat4));

    }

    @Test
    @DisplayName("유효한 토큰으로 예약 성공")
    @Transactional
    void testReserveSuccess() {
        // given
        Long userId = 1L;
        String token = "valid_token";
        List<Long> seatIdList = List.of(1L, 2L);

        ReserveCommand.Reserve command = ReserveCommand.Reserve.builder()
                .userId(userId)
                .seatIdList(seatIdList)
                .token(token)
                .build();

        // when
        ReserveInfo.Reserve result = reserveFacade.reserve(command);

        // then
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(seatIdList, result.getSeatIdList());
        assertEquals(token, result.getToken());
    }

    @Test
    @DisplayName("이미 예약된 좌석일 시 예외 발생")
    @Transactional
    void testSeatAlreadyReserved() {
        // given
        Long userId = 1L;
        String token = "valid_token";
        List<Long> seatIdList = List.of(1L, 3L);

        ReserveCommand.Reserve command = ReserveCommand.Reserve.builder()
                .userId(userId)
                .seatIdList(seatIdList)
                .token(token)
                .build();

        // when / then
        assertThrows(IllegalStateException.class, () -> reserveFacade.reserve(command));
    }

    @Test
    @DisplayName("이미 확정된 좌석일 시 예외 발생")
    @Transactional
    void testSeatAlreadyConfirmed() {
        // given
        Long userId = 1L;
        String token = "valid_token";
        List<Long> seatIdList = List.of(1L, 4L);

        ReserveCommand.Reserve command = ReserveCommand.Reserve.builder()
                .userId(userId)
                .seatIdList(seatIdList)
                .token(token)
                .build();

        // when / then
        assertThrows(IllegalStateException.class, () -> reserveFacade.reserve(command));
    }

}