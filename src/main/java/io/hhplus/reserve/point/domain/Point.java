package io.hhplus.reserve.point.domain;

import io.hhplus.reserve.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "point")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long pointId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "point")
    @ColumnDefault("0")
    private int point;

    @Builder(builderMethodName = "createBuilder")
    public Point(Long userId, int point) {
        this.userId = userId;
        this.point = point;
    }

    public static Point createPoint(PointCommand.Action command) {
        return Point.createBuilder()
                .userId(command.getUserId())
                .point(command.getPoint())
                .build();
    }

    public void chargePoint(int point) {
        if (point <= 0) {
            throw new IllegalArgumentException("충전 포인트는 0보다 커야합니다.");
        }

        this.point += point;
    }

    public void usePoint(int point) {
        if (point <= 0) {
            throw new IllegalArgumentException("사용 포인트는 0보다 커야합니다.");
        }

        if (this.point < point) {
            throw new IllegalStateException("보유 포인트가 부족합니다.");
        }

        this.point -= point;
    }

}
