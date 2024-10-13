package io.hhplus.reserve.concert.domain;

import io.hhplus.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "CONCERT_SEAT")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConcertSeat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    private Long seatId;

    @Column(name = "CONCERT_ID")
    private Long concertId;

    @Column(name = "SEAT_NUM")
    private int seatNum;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'AVAILABLE'")
    private SeatStatus status;

    @Column(name = "RESERVED_AT")
    private LocalDateTime reservedAt;

}
