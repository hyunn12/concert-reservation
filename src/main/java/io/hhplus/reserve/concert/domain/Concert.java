package io.hhplus.reserve.concert.domain;

import io.hhplus.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "CONCERT")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Concert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONCERT_ID")
    private Long concertId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CONCERT_START_AT")
    private LocalDateTime concertStartAt;

    @Column(name = "CONCERT_END_AT")
    private LocalDateTime concertEndAt;

    @Column(name = "RESERVATION_START_AT")
    private LocalDateTime reservationStartAt;

    @Column(name = "RESERVATION_END_AT")
    private LocalDateTime reservationEndAt;

}
