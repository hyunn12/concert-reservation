package io.hhplus.reserve.payment.application;

import io.hhplus.reserve.config.annotation.Facade;
import io.hhplus.reserve.payment.domain.PaymentDomainService;
import io.hhplus.reserve.point.application.PointCommand;
import io.hhplus.reserve.point.domain.PointDomainService;
import io.hhplus.reserve.waiting.domain.WaitingDomainService;
import jakarta.transaction.Transactional;

@Facade
public class PaymentFacade {

    private final PaymentDomainService paymentDomainService;
    private final WaitingDomainService waitingDomainService;
    private final PointDomainService pointDomainService;

    public PaymentFacade(PaymentDomainService paymentDomainService, WaitingDomainService waitingDomainService, PointDomainService pointDomainService) {
        this.paymentDomainService = paymentDomainService;
        this.waitingDomainService = waitingDomainService;
        this.pointDomainService = pointDomainService;
    }

    @Transactional
    public PaymentInfo.Main pay(PaymentCommand.Payment command) {

        PaymentCriteria.Main criteria = PaymentCriteria.Main.create(command);

        // 토큰 유효성 검사
        waitingDomainService.validateToken(criteria.getToken());

        // 포인트 사용
        PointCommand.Action pointCommand = PointCommand.Action.builder()
                .userId(criteria.getUserId())
                .point(criteria.getAmount())
                .build();
        pointDomainService.usePoint(pointCommand);

        // 예약
        return paymentDomainService.pay(criteria);
    }

}
