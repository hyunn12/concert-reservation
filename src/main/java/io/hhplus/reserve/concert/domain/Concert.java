package io.hhplus.reserve.concert.domain;

import io.hhplus.reserve.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "concert")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Concert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_id")
    private Long concertId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "concert_start_at")
    private LocalDateTime concertStartAt;

    @Column(name = "concert_end_at")
    private LocalDateTime concertEndAt;

    @Column(name = "reservation_start_at")
    private LocalDateTime reservationStartAt;

    @Column(name = "reservation_end_at")
    private LocalDateTime reservationEndAt;

}
