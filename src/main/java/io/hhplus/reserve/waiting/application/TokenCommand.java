package io.hhplus.reserve.waiting.application;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenCommand {

    @Getter
    @Builder
    public static class Generate {
        private Long userId;
        private Long concertId;
    }

    @Getter
    @Builder
    public static class Status {
        private String token;
    }

}
