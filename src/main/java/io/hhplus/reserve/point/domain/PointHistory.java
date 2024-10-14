package io.hhplus.reserve.point.domain;

import io.hhplus.reserve.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "point_history")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "point_id")
    private Long pointId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'CHARGE'")
    private PointHistoryType type;

    @Column(name = "amount")
    @ColumnDefault("0")
    private int amount;

}
