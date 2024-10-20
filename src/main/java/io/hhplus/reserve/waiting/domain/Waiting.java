package io.hhplus.reserve.waiting.domain;

import io.hhplus.reserve.common.domain.BaseEntity;
import io.hhplus.reserve.common.exception.BusinessException;
import io.hhplus.reserve.common.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.UUID;

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
        this.token = UUID.randomUUID().toString();
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

    // 대기인원 확인
    public void activateStatusNoWaiting(int count) {
        if (count == 0) {
            this.status = WaitingStatus.ACTIVE;
        }
    }

    // 유효성 검증
    public void validateToken() {
        if (this.status == WaitingStatus.DELETE) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
    }

    // 토큰 삭제
    public void deleteToken() {
        this.status = WaitingStatus.DELETE;
        super.setDeletedAt(LocalDateTime.now());
    }

}
