package io.hhplus.reserve.reservation.domain;

import io.hhplus.reserve.reservation.application.ReserveCriteria;
import io.hhplus.reserve.reservation.application.ReserveInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationDomainService {

    private final ReserveRepository reserveRepository;

    public ReservationDomainService(ReserveRepository reserveRepository) {
        this.reserveRepository = reserveRepository;
    }

    public ReserveInfo.Reserve reserve(ReserveCriteria.Reserve criteria) {

        Reservation savedReservation = Reservation.createReservation(
                criteria.getUserId(),
                criteria.getConcert().getTitle(),
                criteria.getConcert().getConcertStartAt(),
                criteria.getConcert().getConcertEndAt()
        );

        Reservation reservation = reserveRepository.generateReservation(savedReservation);

        List<ReservationItem> itemList = ReservationItem.assignItemList(reservation.getReservationId(), criteria.getSeatList());

        reserveRepository.generateReservationItemList(itemList);

        return ReserveInfo.Reserve.of(reservation);
    }

}
