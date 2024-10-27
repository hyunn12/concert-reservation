package io.hhplus.reserve.reservation.application;

import io.hhplus.reserve.common.annotation.Facade;
import io.hhplus.reserve.concert.domain.ConcertSeat;
import io.hhplus.reserve.concert.domain.ConcertService;
import io.hhplus.reserve.reservation.domain.ReserveCommand;
import io.hhplus.reserve.reservation.domain.ReserveInfo;
import jakarta.transaction.Transactional;

import java.util.List;

@Facade
public class ReserveFacade {

    private final ConcertService concertService;

    public ReserveFacade(ConcertService concertService) {
        this.concertService = concertService;
    }

    // 좌석 선점
    @Transactional
    public ReserveInfo.Reserve reserve(ReserveCommand.Reserve command) {

        ReserveCriteria.Main criteria = ReserveCriteria.Main.create(command);

        // 좌석 예약 상태 확인 및 선점
        List<ConcertSeat> seatList = concertService.getSeatListWithLock(criteria.getSeatIdList());
        concertService.reserveSeat(seatList);

        return ReserveInfo.Reserve.of(criteria.getUserId(), criteria.getSeatIdList());
    }

}
