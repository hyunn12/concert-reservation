package io.hhplus.reserve.payment.domain;

import io.hhplus.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "PAYMENT")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @Column(name = "RESERVATION_ID")
    private Long reservationId;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "PAYMENT_AMOUNT")
    private int paymentAmount;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'SUCCESS'")
    private PaymentStatus status;

}
