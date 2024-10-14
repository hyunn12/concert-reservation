package io.hhplus.reserve.waiting.domain;

import io.hhplus.reserve.common.domain.BaseEntity;
import io.hhplus.reserve.waiting.application.TokenCommand;
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

    @Setter
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'WAIT'")
    private WaitingStatus status;

    @Builder(builderMethodName = "createTokenBuilder")
    public Waiting(TokenCommand.Generate command) {
        this.userId = command.getUserId();
        this.concertId = command.getConcertId();
    }

    @Builder(builderMethodName = "refreshTokenBuilder")
    public Waiting(TokenCommand.Status command) {
        this.userId = command.getUserId();
        this.token = command.getToken();
    }

}
