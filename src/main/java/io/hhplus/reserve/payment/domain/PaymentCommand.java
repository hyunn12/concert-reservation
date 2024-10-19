package io.hhplus.reserve.payment.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentCommand {

    @Getter
    @Builder
    public static class Payment {
        private String token;
        private Long userId;
        private List<Long> seatIdList;
        private Long reservationId;
        private int amount;
    }

}
