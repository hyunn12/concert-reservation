package io.hhplus.reserve.concert.domain;

import io.hhplus.reserve.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "concert_seat")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConcertSeat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long seatId;

    @Column(name = "concert_id")
    private Long concertId;

    @Column(name = "seat_num")
    private int seatNum;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'AVAILABLE'")
    private SeatStatus status;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

}