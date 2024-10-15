package io.hhplus.reserve.waiting.domain;

import io.hhplus.reserve.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "waiting")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Waiting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waiting_id")
    private Long waitingId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "concert_id")
    private Long concertId;

    @Column(name = "token")
    private String token;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'WAIT'")
    private WaitingStatus status;

    @Builder(builderMethodName = "createTokenBuilder")
    public Waiting(Long userId, Long concertId, WaitingStatus status) {
        this.userId = userId;
        this.concertId = concertId;
        this.status = status;
    }

    @Builder(builderMethodName = "refreshTokenBuilder")
    public Waiting(String token, WaitingStatus status) {
        this.token = token;
        this.status = status;
    }

    // 토큰 생성
    public static Waiting createToken(Long userId, Long concertId, WaitingStatus status) {
        return Waiting.createTokenBuilder()
                .userId(userId)
                .concertId(concertId)
                .status(status)
                .build();
    }

    // 토큰 갱신
    public void refreshStatus(WaitingStatus newStatus) {
        this.status = newStatus;
    }

}
