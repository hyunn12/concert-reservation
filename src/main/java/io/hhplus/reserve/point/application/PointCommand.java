package io.hhplus.reserve.point.application;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointCommand {

    @Getter
    @Builder
    public static class Action {
        private Long userId;
        private int point;
    }

}
