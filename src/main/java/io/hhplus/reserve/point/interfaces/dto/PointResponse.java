package io.hhplus.reserve.point.interfaces.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointResponse {

    @Getter
    @Builder
    public static class Point {
        private Long userId;
        private int point;
    }

    @Getter
    @Builder
    public static class Charge {
        private int point;
    }

}
