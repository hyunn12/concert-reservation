package io.hhplus.reserve.reservation.application;

import io.hhplus.reserve.common.annotation.Facade;
import io.hhplus.reserve.concert.domain.ConcertSeat;
import io.hhplus.reserve.concert.domain.ConcertService;
import io.hhplus.reserve.reservation.domain.ReserveCommand;
import io.hhplus.reserve.reservation.domain.ReserveInfo;
import io.hhplus.reserve.support.domain.exception.BusinessException;
import io.hhplus.reserve.support.domain.exception.ErrorType;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Facade
public class ReserveFacade {

    private final ConcertService concertService;

    public ReserveFacade(ConcertService concertService) {
        this.concertService = concertService;
    }

    // 좌석 선점
    @Transactional
    public ReserveInfo.Reserve reserve(ReserveCommand.Reserve command) {
        try {
            ReserveCriteria.Main criteria = ReserveCriteria.Main.create(command);

            // 좌석 예약 상태 확인 및 선점
            List<ConcertSeat> seatList = concertService.getSeatListWithLock(criteria.getSeatIdList());
            concertService.reserveSeat(seatList);

            return ReserveInfo.Reserve.of(criteria.getUserId(), criteria.getSeatIdList());

        } catch (OptimisticLockException e) {
            throw new BusinessException(ErrorType.INVALID_SEAT);
        }
    }

    // Redis 분산락 활용한 좌석 선점
    @Transactional
    public ReserveInfo.Reserve reserveWithRedis(ReserveCommand.Reserve command) {
        ReserveCriteria.Main criteria = ReserveCriteria.Main.create(command);

        // 좌석 예약 상태 확인 및 선점
        List<ConcertSeat> seatList = criteria.getSeatIdList().stream()
                .map(seatId -> concertService.getConcertSeatWithRedis(command.getConcertId(), seatId))
                .toList();
        concertService.reserveSeat(seatList);

        return ReserveInfo.Reserve.of(criteria.getUserId(), criteria.getSeatIdList());
    }

}
