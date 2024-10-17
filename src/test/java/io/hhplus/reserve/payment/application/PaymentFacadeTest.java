package io.hhplus.reserve.payment.application;

import io.hhplus.reserve.payment.domain.Payment;
import io.hhplus.reserve.payment.domain.PaymentDomainService;
import io.hhplus.reserve.payment.infra.PaymentJpaRepository;
import io.hhplus.reserve.waiting.domain.WaitingDomainService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PaymentFacadeTest {

    @Autowired
    private PaymentDomainService paymentDomainService;
    @Autowired
    private WaitingDomainService waitingDomainService;
    @Autowired
    private PaymentJpaRepository paymentRepository;

    private PaymentFacade paymentFacade;

    @BeforeEach
    void setUp() {
        paymentFacade = new PaymentFacade(paymentDomainService, waitingDomainService);
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

        // 결제가 DB에 성공적으로 저장되었는지 확인
        Payment savedPayment = paymentRepository.findById(result.getPaymentId()).orElse(null);
        assertNotNull(savedPayment);
        assertEquals(savedPayment.getPaymentAmount(), 10000);
    }

    @Test
    @DisplayName("DELETE 토큰으로 결제")
    void shouldFailWhenTokenIsDeleted() {
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

        // 결제가 DB에 저장되지 않음 확인
        assertEquals(paymentRepository.findAll().size(), initialPaymentCount);
    }

}