package io.hhplus.reserve.point.domain;

import io.hhplus.reserve.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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

}
