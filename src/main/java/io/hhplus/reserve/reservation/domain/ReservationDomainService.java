package io.hhplus.reserve.reservation.domain;

import io.hhplus.reserve.reservation.application.ReserveCommand;
import io.hhplus.reserve.reservation.application.ReserveInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationDomainService {

    private final ReserveRepository reserveRepository;

    public ReservationDomainService(ReserveRepository reserveRepository) {
        this.reserveRepository = reserveRepository;
    }

    public ReserveInfo.Reserve reserve(ReserveCommand.Reservation command) {

        Reservation savedReservation = Reservation.createReservation(
                command.getUserId(),
                command.getConcertTitle(),
                command.getConcertStartAt(),
                command.getConcertEndAt()
        );

        Reservation reservation = reserveRepository.generateReservation(savedReservation);

        List<ReservationItem> itemList = ReservationItem.assignItemList(reservation.getReservationId(), command.getSeatIdList());

        reserveRepository.generateReservationItemList(itemList);

        return ReserveInfo.Reserve.of(reservation);
    }

}
