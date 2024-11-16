package io.hhplus.reserve.external.application;

import io.hhplus.reserve.payment.domain.PaymentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExternalService {

    public void notifyPaymentSuccess(PaymentInfo.Main info) {
        log.info("notifyPaymentSuccess::: {}", info);
    }

}
