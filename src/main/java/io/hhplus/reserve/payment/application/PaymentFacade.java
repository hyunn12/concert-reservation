package io.hhplus.reserve.payment.application;

import io.hhplus.reserve.config.annotation.Facade;
import io.hhplus.reserve.payment.domain.PaymentCommand;
import io.hhplus.reserve.payment.domain.PaymentCriteria;
import io.hhplus.reserve.payment.domain.PaymentService;
import io.hhplus.reserve.payment.domain.PaymentInfo;
import io.hhplus.reserve.point.domain.PointCommand;
import io.hhplus.reserve.point.domain.PointService;
import io.hhplus.reserve.waiting.domain.WaitingService;
import jakarta.transaction.Transactional;

@Facade
public class PaymentFacade {

    private final PaymentService paymentService;
    private final WaitingService waitingService;
    private final PointService pointService;

    public PaymentFacade(PaymentService paymentService, WaitingService waitingService, PointService pointService) {
        this.paymentService = paymentService;
        this.waitingService = waitingService;
        this.pointService = pointService;
    }

    @Transactional
    public PaymentInfo.Main pay(PaymentCommand.Payment command) {

        PaymentCriteria.Main criteria = PaymentCriteria.Main.create(command);

        // 토큰 유효성 검사
        waitingService.validateToken(criteria.getToken());

        // 포인트 사용
        PointCommand.Action pointCommand = PointCommand.Action.builder()
                .userId(criteria.getUserId())
                .point(criteria.getAmount())
                .build();
        pointService.usePoint(pointCommand);

        // 예약
        return paymentService.pay(criteria);
    }

}
