package io.hhplus.reserve.reservation.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReserveCommand {

    @Getter
    @Builder
    public static class Reserve {
        private Long userId;
        private Long concertId;
        private List<Long> seatIdList;
    }

}
