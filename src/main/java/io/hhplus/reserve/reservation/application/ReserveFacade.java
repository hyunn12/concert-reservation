package io.hhplus.reserve.reservation.application;

import io.hhplus.reserve.concert.domain.Concert;
import io.hhplus.reserve.concert.domain.ConcertService;
import io.hhplus.reserve.concert.domain.ConcertSeat;
import io.hhplus.reserve.config.annotation.Facade;
import io.hhplus.reserve.reservation.domain.ReservationService;
import io.hhplus.reserve.reservation.domain.ReserveCommand;
import io.hhplus.reserve.reservation.domain.ReserveCriteria;
import io.hhplus.reserve.reservation.domain.ReserveInfo;
import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingService;
import jakarta.transaction.Transactional;

import java.util.List;

@Facade
public class ReserveFacade {

    private final ConcertService concertService;
    private final ReservationService reservationService;
    private final WaitingService waitingService;

    public ReserveFacade(ConcertService concertService, ReservationService reservationService, WaitingService waitingService) {
        this.concertService = concertService;
        this.reservationService = reservationService;
        this.waitingService = waitingService;
    }

    @Transactional
    public ReserveInfo.Reserve reserve(ReserveCommand.Reserve command) {

        ReserveCriteria.Main criteria = ReserveCriteria.Main.create(command);

        // 토큰 유효성 검사
        Waiting waiting = waitingService.validateToken(criteria.getToken());

        // 좌석 예약 상태 확인 및 선점
        List<ConcertSeat> seatList = concertService.getSeatListWithLock(criteria.getSeatIdList());
        concertService.hasInvalidSeat(seatList);

        // 예약
        Concert concert = concertService.getConcertDetail(waiting.getConcertId());

        ReserveCriteria.Reserve reserveCriteria = ReserveCriteria.Reserve.create(criteria.getUserId(), seatList, concert);

        // 토큰 삭제
        waitingService.deleteToken(waiting);

        return reservationService.reserve(reserveCriteria);
    }

}
