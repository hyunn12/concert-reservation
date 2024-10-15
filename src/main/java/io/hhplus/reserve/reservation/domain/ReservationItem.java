package io.hhplus.reserve.reservation.domain;

import io.hhplus.reserve.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Builder(builderMethodName = "createBuilder")
    public ReservationItem(long reservationId, long seatId) {
        this.reservationId = reservationId;
        this.seatId = seatId;
    }

    public static ReservationItem createItem(long reservationId, long seatId) {
        return ReservationItem.createBuilder()
                .reservationId(reservationId)
                .seatId(seatId)
                .build();
    }

    public static List<ReservationItem> assignItemList(long reservationId, List<Long> seatIdList) {
        return seatIdList.stream().map(seatId -> ReservationItem.createItem(reservationId, seatId)).toList();
    }

}
