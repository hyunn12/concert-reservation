package io.hhplus.reserve.point.interfaces.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "PointRequest.Charge", description = "포인트 충전 요청 객체")
    public static class Charge {

        @NotNull
        @Schema(description = "회원 ID", example = "1")
        private Long userId;

        @NotNull
        @Positive
        @Schema(description = "포인트", example = "30000")
        private int point;

    }

}
