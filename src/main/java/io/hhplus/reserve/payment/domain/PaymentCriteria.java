package io.hhplus.reserve.payment.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentCriteria {

    @Getter
    @Builder
    public static class Main {
        private String token;
        private Long userId;
        private List<Long> seatIdList;
        private Long reservationId;
        private int amount;

        public static PaymentCriteria.Main create(PaymentCommand.Payment command) {
            return PaymentCriteria.Main.builder()
                    .token(command.getToken())
                    .userId(command.getUserId())
                    .seatIdList(command.getSeatIdList())
                    .reservationId(command.getReservationId())
                    .amount(command.getAmount())
                    .build();
        }

        public void updateReservationId(Long reservationId) {
            this.reservationId = reservationId;
        }
    }

}
