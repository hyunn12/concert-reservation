package io.hhplus.reserve.payment.application;

import io.hhplus.reserve.config.annotation.Facade;
import io.hhplus.reserve.payment.domain.PaymentDomainService;
import io.hhplus.reserve.waiting.domain.WaitingDomainService;
import jakarta.transaction.Transactional;

@Facade
public class PaymentFacade {

    private final PaymentDomainService paymentDomainService;
    private final WaitingDomainService waitingDomainService;

    public PaymentFacade(PaymentDomainService paymentDomainService, WaitingDomainService waitingDomainService) {
        this.paymentDomainService = paymentDomainService;
        this.waitingDomainService = waitingDomainService;
    }

    @Transactional
    public PaymentInfo.Main pay(PaymentCommand.Payment command) {

        PaymentCriteria.Main criteria = PaymentCriteria.Main.create(command);

        // 토큰 유효성 검사
        waitingDomainService.validateToken(criteria.getToken());

        // 예약
        return paymentDomainService.pay(criteria);
    }

}
