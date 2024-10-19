package io.hhplus.reserve.reservation.infra;

import io.hhplus.reserve.reservation.domain.Reservation;
import io.hhplus.reserve.reservation.domain.ReservationItem;
import io.hhplus.reserve.reservation.domain.ReserveRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReserveRepositoryImpl implements ReserveRepository {

    private final ReservationJpaRepository reservationJpaRepository;
    private final ReservationItemJpaRepository reservationItemJpaRepository;

    public ReserveRepositoryImpl(ReservationJpaRepository reservationJpaRepository,
                            ReservationItemJpaRepository reservationItemJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
        this.reservationItemJpaRepository = reservationItemJpaRepository;
    }

    @Override
    public Reservation generateReservation(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

    @Override
    public List<ReservationItem> generateReservationItemList(List<ReservationItem> itemList) {
        return reservationItemJpaRepository.saveAll(itemList);
    }

}
