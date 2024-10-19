package io.hhplus.reserve.reservation.domain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReserveRepository reserveRepository;

    public ReservationService(ReserveRepository reserveRepository) {
        this.reserveRepository = reserveRepository;
    }

    public Reservation reserve(ReserveCriteria.Reserve criteria) {

        Reservation savedReservation = Reservation.createReservation(
                criteria.getUserId(),
                criteria.getConcert().getTitle(),
                criteria.getConcert().getConcertStartAt(),
                criteria.getConcert().getConcertEndAt()
        );

        Reservation reservation = reserveRepository.generateReservation(savedReservation);

        List<ReservationItem> itemList = ReservationItem.assignItemList(reservation.getReservationId(), criteria.getSeatList());

        reserveRepository.generateReservationItemList(itemList);

        return reservation;
    }

}
