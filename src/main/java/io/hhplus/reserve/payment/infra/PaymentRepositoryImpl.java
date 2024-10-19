package io.hhplus.reserve.payment.infra;

import io.hhplus.reserve.payment.domain.Payment;
import io.hhplus.reserve.payment.domain.PaymentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    public PaymentRepositoryImpl(PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    public Payment createPayment(Payment payment) {
        return paymentJpaRepository.save(payment);
    }
}
