package io.hhplus.reserve.waiting.interfaces.dto;

import io.hhplus.reserve.waiting.domain.TokenCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenRequest {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Schema(name = "TokenRequest.Generate", description = "토큰 생성 요청 객체")
    public static class Generate {

        @NotNull
        @Schema(description = "회원 ID", example = "1")
        private Long userId;

        @NotNull
        @Schema(description = "콘서트 ID", example = "1")
        private Long concertId;

        public TokenCommand.Generate toCommand() {
            return TokenCommand.Generate.builder()
                    .userId(userId)
                    .concertId(concertId)
                    .build();
        }

    }

    @Getter
    @Builder
    @Schema(name = "TokenRequest.Status", description = "토큰 상태 조회 요청 객체")
    public static class Status {

        @NotNull
        @Schema(description = "토큰", example = "valid_token")
        private String token;

        public TokenCommand.Status toCommand() {
            return TokenCommand.Status.builder()
                    .token(token)
                    .build();
        }
    }
}
