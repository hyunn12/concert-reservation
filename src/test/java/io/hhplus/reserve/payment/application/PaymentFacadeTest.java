package io.hhplus.reserve.payment.application;

import io.hhplus.reserve.payment.domain.Payment;
import io.hhplus.reserve.payment.domain.PaymentCommand;
import io.hhplus.reserve.payment.domain.PaymentService;
import io.hhplus.reserve.payment.domain.PaymentInfo;
import io.hhplus.reserve.payment.infra.PaymentJpaRepository;
import io.hhplus.reserve.point.domain.PointService;
import io.hhplus.reserve.waiting.domain.WaitingService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PaymentFacadeTest {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private WaitingService waitingService;
    @Autowired
    private PointService pointService;
    @Autowired
    private PaymentJpaRepository paymentRepository;

    private PaymentFacade paymentFacade;

    @BeforeEach
    void setUp() {
        paymentFacade = new PaymentFacade(paymentService, waitingService, pointService);
    }

    @Test
    @DisplayName("유효한 토큰으로 결제 성공")
    void paySuccessfully() {
        // given
        PaymentCommand.Payment command = PaymentCommand.Payment.builder()
                .userId(1L)
                .reservationId(1L)
                .amount(10000)
                .token("testtokentokentoken")
                .build();

        // when
        PaymentInfo.Main result = paymentFacade.pay(command);

        // then
        assertNotNull(result);
        assertEquals(result.getPaymentAmount(), 10000);
        assertEquals(result.getStatus(), "SUCCESS");

        Payment savedPayment = paymentRepository.findById(result.getPaymentId()).orElse(null);
        assertNotNull(savedPayment);
        assertEquals(savedPayment.getPaymentAmount(), 10000);
    }

    @Test
    @DisplayName("DELETE 토큰으로 결제")
    void testDeleteToken() {
        // given
        int initialPaymentCount = paymentRepository.findAll().size();
        PaymentCommand.Payment command = PaymentCommand.Payment.builder()
                .userId(1L)
                .reservationId(1L)
                .amount(10000)
                .token("testtokentokentoken2")
                .build();

        // when / then
        assertThrows(IllegalStateException.class, () -> paymentFacade.pay(command));

        assertEquals(paymentRepository.findAll().size(), initialPaymentCount);
    }

    @Test
    @DisplayName("소지 포인트보다 많이 사용")
    void testUseOverPoint() {
        int initialPaymentCount = paymentRepository.findAll().size();
        PaymentCommand.Payment command = PaymentCommand.Payment.builder()
                .userId(2L)
                .reservationId(1L)
                .amount(5000)
                .token("testtokenuser2")
                .build();

        assertThrows(IllegalStateException.class, () -> paymentFacade.pay(command));

        List<Payment> paymentList = paymentRepository.findAll();
        assertEquals(paymentRepository.findAll().size(), initialPaymentCount);
    }

}