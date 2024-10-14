package io.hhplus.reserve.waiting.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenInfo {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Token {
        private String token;
        private String status;

        public static Token of(Waiting waiting) {
            return new Token(waiting.getToken(), waiting.getStatus().toString());
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Status {
        private String token;
        private String status;
        private int waitingCount;

        public static Status of(Waiting waiting, int waitingCount) {
            return new Status(waiting.getToken(), waiting.getStatus().toString(), waitingCount);
        }
    }

}
