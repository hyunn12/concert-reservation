package io.hhplus.reserve.point.interfaces.dto;

import io.hhplus.reserve.point.domain.PointInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointResponse {

    @Getter
    @Builder
    @Schema(name = "PointResponse.Point", description = "포인트 조회 결과 객체")
    public static class Point {

        @Schema(description = "포인트", example = "40000")
        private int point;

        public static Point of(PointInfo.Main info) {
            return Point.builder().point(info.getPoint()).build();
        }

    }

}
