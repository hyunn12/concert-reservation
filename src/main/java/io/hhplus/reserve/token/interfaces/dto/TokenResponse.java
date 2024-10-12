package io.hhplus.reserve.token.interfaces.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenResponse {

    @Getter
    @Builder
    public static class Token {
        private Long waitingId;
        private Long userId;
        private String token;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;
    }

}
