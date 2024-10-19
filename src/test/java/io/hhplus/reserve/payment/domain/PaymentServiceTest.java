package io.hhplus.reserve.payment.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("결제 성공")
    void testPay() {
        // given
        PaymentCriteria.Main command = PaymentCriteria.Main.builder()
                .reservationId(1L)
                .userId(1L)
                .amount(10000)
                .build();

        Payment payment = Payment.createPayment(command);

        given(paymentRepository.createPayment(any(Payment.class))).willReturn(payment);

        // when
        PaymentInfo.Main result = paymentService.pay(command);

        // then
        assertNotNull(result);
        assertEquals(result.getUserId(), payment.getUserId());
        assertEquals(result.getPaymentAmount(), command.getAmount());

        then(paymentRepository).should(times(1)).createPayment(any(Payment.class));
    }


}