package io.hhplus.reserve.payment.domain;

import io.hhplus.reserve.payment.application.PaymentCriteria;
import io.hhplus.reserve.payment.application.PaymentInfo;
import org.springframework.stereotype.Service;

@Service
public class PaymentDomainService {

    private final PaymentRepository paymentRepository;

    public PaymentDomainService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentInfo.Main pay(PaymentCriteria.Main criteria) {
        Payment payment = Payment.createPayment(criteria);
        paymentRepository.createPayment(payment);
        return PaymentInfo.Main.of(payment);
    }

}
