package io.hhplus.reserve.point.application;

import io.hhplus.reserve.point.domain.Point;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointInfo {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Main {
        private int point;

        public static Main of(Point point) {
            return new Main(point.getPoint());
        }
    }

}
