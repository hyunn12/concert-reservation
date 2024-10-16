package io.hhplus.reserve.payment.domain;

import io.hhplus.reserve.payment.application.PaymentCommand;
import io.hhplus.reserve.payment.application.PaymentInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PaymentDomainServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentDomainService paymentDomainService;

    @Test
    @DisplayName("결제 성공")
    void testPay() {
        // given
        PaymentCommand.Payment command = PaymentCommand.Payment.builder()
                .reservationId(1L)
                .userId(1L)
                .amount(10000)
                .build();

        Payment payment = Payment.createPayment(command);

        given(paymentRepository.createPayment(any(Payment.class))).willReturn(payment);

        // when
        PaymentInfo.Main result = paymentDomainService.pay(command);

        // then
        assertThat(result).isNotNull();
        assertEquals(result.getUserId(), payment.getUserId());
        assertEquals(result.getPaymentAmount(), command.getAmount());

        then(paymentRepository).should(times(1)).createPayment(any(Payment.class));
    }


}