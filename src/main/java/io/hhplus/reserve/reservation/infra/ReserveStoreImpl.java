package io.hhplus.reserve.reservation.infra;

import io.hhplus.reserve.reservation.domain.Reservation;
import io.hhplus.reserve.reservation.domain.ReservationItem;
import io.hhplus.reserve.reservation.domain.ReserveStore;
import org.springframework.stereotype.Repository;

@Repository
public class ReserveStoreImpl implements ReserveStore {

    private final ReservationJpaRepository reservationJpaRepository;
    private final ReservationItemJpaRepository reservationItemJpaRepository;

    public ReserveStoreImpl(ReservationJpaRepository reservationJpaRepository,
                            ReservationItemJpaRepository reservationItemJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
        this.reservationItemJpaRepository = reservationItemJpaRepository;
    }

    public Reservation generateReservation(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

    public ReservationItem generateReservationItem(ReservationItem reservationItem) {
        return reservationItemJpaRepository.save(reservationItem);
    }

}
