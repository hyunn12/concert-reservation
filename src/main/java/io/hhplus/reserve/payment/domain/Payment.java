package io.hhplus.reserve.payment.domain;

import io.hhplus.reserve.common.domain.BaseEntity;
import io.hhplus.reserve.payment.application.PaymentCriteria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "payment")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "payment_amount")
    private int paymentAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'SUCCESS'")
    private PaymentStatus status;

    @Builder(builderMethodName = "createBuilder")
    public Payment(Long reservationId, Long userId, int paymentAmount) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.paymentAmount = paymentAmount;
        this.status = PaymentStatus.SUCCESS;
    }

    public static Payment createPayment(PaymentCriteria.Main criteria) {
        return Payment.createBuilder()
                .reservationId(criteria.getReservationId())
                .userId(criteria.getUserId())
                .paymentAmount(criteria.getAmount())
                .build();
    }

}
