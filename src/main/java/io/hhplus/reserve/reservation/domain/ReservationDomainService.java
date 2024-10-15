package io.hhplus.reserve.reservation.domain;

import io.hhplus.reserve.reservation.application.ReserveCommand;
import org.springframework.stereotype.Service;

@Service
public class ReservationDomainService {

    private final ReserveStore reserveStore;

    public ReservationDomainService(ReserveStore reserveStore) {
        this.reserveStore = reserveStore;
    }

    public Reservation reserve(ReserveCommand.Reservation command) {

        Reservation savedReservation = Reservation.createBuilder()
                .userId(command.getUserId())
                .concertTitle(command.getConcertTitle())
                .concertStartAt(command.getConcertStartAt())
                .concertEndAt(command.getConcertEndAt())
                .build();

        Reservation reservation = reserveStore.generateReservation(savedReservation);

        for (Long seatId : command.getSeatIdList()) {
            ReservationItem item = ReservationItem.createBuilder()
                    .reservationId(reservation.getReservationId())
                    .seatId(seatId)
                    .build();

            reserveStore.generateReservationItem(item);
        }

        return reservation;
    }

}
