package io.hhplus.reserve.external.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExternalService {

    public void notifyPaymentSuccess(Long paymentId) {
        log.info("# [ExternalService] notifyPaymentSuccess::: paymentId: {}", paymentId);
    }
}
