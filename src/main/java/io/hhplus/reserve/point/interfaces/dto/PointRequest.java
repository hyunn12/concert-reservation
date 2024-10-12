package io.hhplus.reserve.point.interfaces.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointRequest {

    @Getter
    @Builder
    public static class Charge {

        @NotNull
        private Long userId;

        @NotNull
        @Positive
        private int point;

    }

}
