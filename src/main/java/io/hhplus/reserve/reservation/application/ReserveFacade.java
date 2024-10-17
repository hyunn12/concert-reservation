package io.hhplus.reserve.reservation.application;

import io.hhplus.reserve.concert.domain.Concert;
import io.hhplus.reserve.concert.domain.ConcertDomainService;
import io.hhplus.reserve.concert.domain.ConcertSeat;
import io.hhplus.reserve.config.annotation.Facade;
import io.hhplus.reserve.reservation.domain.ReservationDomainService;
import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingDomainService;
import jakarta.transaction.Transactional;

import java.util.List;

@Facade
public class ReserveFacade {

    private final ConcertDomainService concertDomainService;
    private final ReservationDomainService reservationDomainService;
    private final WaitingDomainService waitingDomainService;

    public ReserveFacade(ConcertDomainService concertDomainService, ReservationDomainService reservationDomainService, WaitingDomainService waitingDomainService) {
        this.concertDomainService = concertDomainService;
        this.reservationDomainService = reservationDomainService;
        this.waitingDomainService = waitingDomainService;
    }

    @Transactional
    public ReserveInfo.Reserve reserve(ReserveCommand.Reserve command) {

        ReserveCriteria.Main criteria = ReserveCriteria.Main.create(command);
        if (!criteria.isValid()) {
            throw new IllegalStateException("예약조건이 유효하지 않습니다.");
        }

        // 토큰 유효성 검사
        Waiting waiting = waitingDomainService.validateToken(criteria.getToken());

        // 좌석 예약 상태 확인 및 선점
        List<ConcertSeat> seatList = concertDomainService.getSeatListWithLock(criteria.getSeatIdList());


        boolean hasInvalidSeat = concertDomainService.hasInvalidSeat(seatList);
        if (hasInvalidSeat) {
            throw new IllegalStateException("예약이 불가능한 좌석이 포함되어 있습니다.");
        }

        // 예약
        Concert concert = concertDomainService.getConcertDetail(waiting.getConcertId());

        ReserveCriteria.Reserve reserveCriteria = ReserveCriteria.Reserve.create(criteria.getUserId(), seatList, concert);

        return reservationDomainService.reserve(reserveCriteria);
    }

}
