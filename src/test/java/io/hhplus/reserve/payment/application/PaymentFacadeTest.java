package io.hhplus.reserve.payment.application;

import io.hhplus.reserve.concert.domain.Concert;
import io.hhplus.reserve.concert.domain.ConcertSeat;
import io.hhplus.reserve.concert.domain.ConcertService;
import io.hhplus.reserve.concert.domain.SeatStatus;
import io.hhplus.reserve.concert.infra.ConcertJpaRepository;
import io.hhplus.reserve.concert.infra.ConcertSeatJpaRepository;
import io.hhplus.reserve.payment.domain.PaymentCommand;
import io.hhplus.reserve.payment.domain.PaymentInfo;
import io.hhplus.reserve.payment.domain.PaymentService;
import io.hhplus.reserve.point.domain.Point;
import io.hhplus.reserve.point.domain.PointService;
import io.hhplus.reserve.point.infra.PointJpaRepository;
import io.hhplus.reserve.reservation.domain.Reservation;
import io.hhplus.reserve.reservation.domain.ReservationService;
import io.hhplus.reserve.reservation.infra.ReservationJpaRepository;
import io.hhplus.reserve.support.domain.exception.BusinessException;
import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingService;
import io.hhplus.reserve.waiting.infra.WaitingJpaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PaymentFacadeTest {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private WaitingService waitingService;
    @Autowired
    private ConcertService concertService;
    @Autowired
    private PointService pointService;
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private WaitingJpaRepository waitingJpaRepository;
    @Autowired
    private ConcertJpaRepository concertJpaRepository;
    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;
    @Autowired
    private PointJpaRepository pointJpaRepository;
    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    private PaymentFacade paymentFacade;

    @BeforeEach
    void setUp() {
        paymentFacade = new PaymentFacade(paymentService, waitingService, concertService, pointService, reservationService);

        waitingJpaRepository.deleteAll();
        concertJpaRepository.deleteAll();
        concertSeatJpaRepository.deleteAll();
        pointJpaRepository.deleteAll();
        reservationJpaRepository.deleteAll();

        Waiting waiting = new Waiting(1L, 1L, 1L, "valid_token", null);
        waitingJpaRepository.save(waiting);

        Concert concert = new Concert(1L,
                "AA Concert",
                "AA concert desc",
                LocalDateTime.of(2024, 12, 25, 12, 0),
                LocalDateTime.of(2024, 12, 25, 16, 0),
                LocalDateTime.of(2024, 9, 21, 0, 0),
                LocalDateTime.of(2024, 11, 23, 23, 59));
        concertJpaRepository.save(concert);

        ConcertSeat seat1 = new ConcertSeat(1L, 1L, 1, SeatStatus.AVAILABLE, LocalDateTime.now().minusMinutes(3), 0L);
        ConcertSeat seat2 = new ConcertSeat(2L, 1L, 2, SeatStatus.AVAILABLE, LocalDateTime.now().minusMinutes(3), 0L);
        ConcertSeat seat3 = new ConcertSeat(3L, 1L, 3, SeatStatus.AVAILABLE, LocalDateTime.now().minusMinutes(10), 0L);
        concertSeatJpaRepository.saveAll(List.of(seat1, seat2, seat3));

        Point point = new Point(1L, 1L, 10000);
        pointJpaRepository.save(point);
    }


    @Test
    @DisplayName("유효한 조건으로 결제 성공")
    @Transactional
    void testPayment() {
        // given
        PaymentCommand.Payment command = PaymentCommand.Payment.builder()
                .userId(1L)
                .seatIdList(List.of(1L, 2L))
                .token("valid_token")
                .amount(5000)
                .build();

        // when
        PaymentInfo.Main result = paymentFacade.pay(command);

        // then
        assertNotNull(result);
        assertEquals(5000, result.getPaymentAmount());
        assertNotNull(result.getReservationId());

        List<ConcertSeat> seatList = concertSeatJpaRepository.findAllById(List.of(1L, 2L));
        seatList.forEach(seat -> assertEquals(SeatStatus.CONFIRMED, seat.getStatus()));

        Point updatedPoint = pointJpaRepository.findByUserIdWithLock(1L).orElseThrow();
        assertEquals(5000, updatedPoint.getPoint());

        Reservation reservation = reservationJpaRepository.findById(result.getReservationId()).orElseThrow();
        assertNotNull(reservation);
    }

    @Test
    @DisplayName("좌석이 만료된 경우 예외 발생")
    @Transactional
    void testSeatExpired() {
        // given
        PaymentCommand.Payment command = PaymentCommand.Payment.builder()
                .userId(1L)
                .seatIdList(List.of(3L))
                .token("valid_token")
                .amount(5000)
                .build();

        // when / then
        assertThrows(BusinessException.class, () -> paymentFacade.pay(command));
    }

    @Test
    @DisplayName("포인트가 부족한 경우 예외 발생")
    @Transactional
    void testInsufficientPoints() {
        // given
        PaymentCommand.Payment command = PaymentCommand.Payment.builder()
                .userId(1L)
                .seatIdList(List.of(1L, 2L))
                .token("valid_token")
                .amount(15000)
                .build();

        // when / then
        assertThrows(BusinessException.class, () -> paymentFacade.pay(command));
    }

}