package io.hhplus.reserve.payment.domain;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentInfo.Main pay(PaymentCriteria.Main criteria) {
        Payment payment = Payment.createPayment(criteria);
        paymentRepository.createPayment(payment);
        return PaymentInfo.Main.of(payment);
    }

}
