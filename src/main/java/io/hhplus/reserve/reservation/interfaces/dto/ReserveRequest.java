package io.hhplus.reserve.reservation.interfaces.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReserveRequest {

    @Getter
    @Builder
    public static class Reserve {
        @NotNull
        private Long userId;

        @NotNull
        private List<Long> seatIdList;
    }

}
