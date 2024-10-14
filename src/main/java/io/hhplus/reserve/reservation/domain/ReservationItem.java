package io.hhplus.reserve.reservation.domain;

import io.hhplus.reserve.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation_item")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "seat_id")
    private Long seatId;

}
