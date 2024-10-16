package io.hhplus.reserve.payment.domain;

import io.hhplus.reserve.payment.application.PaymentCommand;
import io.hhplus.reserve.payment.application.PaymentInfo;
import org.springframework.stereotype.Service;

@Service
public class PaymentDomainService {

    private final PaymentRepository paymentRepository;

    public PaymentDomainService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentInfo.Main pay(PaymentCommand.Payment command) {
        Payment payment = Payment.createPayment(command);
        paymentRepository.createPayment(payment);
        return PaymentInfo.Main.of(payment);
    }

}
