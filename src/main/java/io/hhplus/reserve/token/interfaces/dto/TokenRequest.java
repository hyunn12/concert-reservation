package io.hhplus.reserve.token.interfaces.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenRequest {

    @Getter
    @Builder
    public static class Token {
        @NotNull
        private Long userId;

        @NotNull
        private String token;
    }

}
