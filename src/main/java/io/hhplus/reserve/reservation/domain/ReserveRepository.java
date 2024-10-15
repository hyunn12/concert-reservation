package io.hhplus.reserve.reservation.domain;

import java.util.List;

public interface ReserveRepository {

    Reservation generateReservation(Reservation reservation);

    List<ReservationItem> generateReservationItemList(List<ReservationItem> itemList);

}
