package io.hhplus.reserve.reservation.domain;

import io.hhplus.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "RESERVATION")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private Long reservationId;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "CONCERT_TITLE")
    private String concertTitle;

    @Column(name = "CONCERT_START_AT")
    private LocalDateTime concertStartAt;

    @Column(name = "CONCERT_END_AT")
    private LocalDateTime concertEndAt;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'SUCCESS'")
    private ReservationStatus status;

}
