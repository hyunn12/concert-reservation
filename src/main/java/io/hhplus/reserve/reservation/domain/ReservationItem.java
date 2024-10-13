package io.hhplus.reserve.reservation.domain;

import io.hhplus.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RESERVATION_ITEM")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long itemId;

    @Column(name = "RESERVATION_ID")
    private Long reservationId;

    @Column(name = "SEAT_ID")
    private Long seatId;

}
