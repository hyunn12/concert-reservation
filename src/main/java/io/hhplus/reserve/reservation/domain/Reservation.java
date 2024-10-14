package io.hhplus.reserve.reservation.domain;

import io.hhplus.reserve.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "concert_title")
    private String concertTitle;

    @Column(name = "concert_start_at")
    private LocalDateTime concertStartAt;

    @Column(name = "concert_end_at")
    private LocalDateTime concertEndAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'SUCCESS'")
    private ReservationStatus status;

}
