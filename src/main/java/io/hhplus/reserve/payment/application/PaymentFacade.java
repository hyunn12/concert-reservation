package io.hhplus.reserve.payment.application;

import io.hhplus.reserve.concert.domain.Concert;
import io.hhplus.reserve.concert.domain.ConcertSeat;
import io.hhplus.reserve.concert.domain.ConcertService;
import io.hhplus.reserve.common.annotation.Facade;
import io.hhplus.reserve.payment.domain.*;
import io.hhplus.reserve.point.domain.PointCommand;
import io.hhplus.reserve.point.domain.PointService;
import io.hhplus.reserve.reservation.domain.Reservation;
import io.hhplus.reserve.reservation.domain.ReservationService;
import io.hhplus.reserve.reservation.application.ReserveCriteria;
import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingService;
import jakarta.transaction.Transactional;

import java.util.List;

@Facade
public class PaymentFacade {

    private final PaymentService paymentService;
    private final WaitingService waitingService;
    private final ConcertService concertService;
    private final PointService pointService;
    private final ReservationService reservationService;

    public PaymentFacade(PaymentService paymentService,
                         WaitingService waitingService,
                         ConcertService concertService,
                         PointService pointService,
                         ReservationService reservationService
    ) {
        this.paymentService = paymentService;
        this.waitingService = waitingService;
        this.concertService = concertService;
        this.pointService = pointService;
        this.reservationService = reservationService;
    }

    // 결제 및 예약
    @Transactional
    public PaymentInfo.Main pay(PaymentCommand.Payment command) {

        PaymentCriteria.Main criteria = PaymentCriteria.Main.create(command);

        // 토큰 유효성 검사
        Waiting waiting = waitingService.validateToken(criteria.getToken());

        // 좌석 선점 시간 확인
        List<ConcertSeat> seatList = concertService.getSeatListWithLock(criteria.getSeatIdList());
        concertService.checkSeatExpired(seatList);

        // 포인트 차감
        PointCommand.Action pointCommand = PointCommand.Action.builder()
                .userId(criteria.getUserId())
                .point(criteria.getAmount())
                .build();
        pointService.usePoint(pointCommand);

        // 예약
        Concert concert = concertService.getConcertDetail(waiting.getConcertId());
        ReserveCriteria.Reserve reserveCriteria = ReserveCriteria.Reserve.create(criteria.getUserId(), seatList, concert);
        Reservation reservation = reservationService.reserve(reserveCriteria);

        // 결제
        criteria.updateReservationId(reservation.getReservationId());
        Payment payment = paymentService.pay(criteria);

        // 좌석 확정
        concertService.confirmSeat(seatList);

        // 토큰 삭제
        waitingService.deleteToken(waiting);

        return PaymentInfo.Main.of(payment, reservation);
    }

}
