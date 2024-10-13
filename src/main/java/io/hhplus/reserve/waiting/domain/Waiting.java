package io.hhplus.reserve.waiting.domain;

import io.hhplus.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "WAITING")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Waiting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WAITING_ID")
    private Long waitingId;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "CONCERT_ID")
    private Long concertId;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'WAIT'")
    private WaitingStatus status;

}
