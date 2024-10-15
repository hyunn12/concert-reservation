package io.hhplus.reserve.reservation.domain;

public interface ReserveStore {

    Reservation generateReservation(Reservation reservation);

    ReservationItem generateReservationItem(ReservationItem reservationItem);

}
