package io.hhplus.reserve.waiting.interfaces.dto;

import io.hhplus.reserve.waiting.application.TokenInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenResponse {

    @Getter
    @Builder
    @Schema(name = "TokenResponse.Token", description = "토큰 생성 결과 객체")
    public static class Token {

        @Schema(description = "token", example = "testtokentokentoken")
        private String token;

        @Schema(description = "대기상태", example = "WAIT")
        private String status;

        public static Token of(TokenInfo.Token info) {
            return Token.builder()
                    .token(info.getToken())
                    .status(info.getStatus())
                    .build();
        }
    }

    @Getter
    @Builder
    @Schema(name = "TokenResponse.Status", description = "토큰 상태 조회 결과 객체")
    public static class Status {

        @Schema(description = "token", example = "testtokentokentoken")
        private String token;

        @Schema(description = "대기상태", example = "WAIT")
        private String status;

        @Schema(description = "대기인원", example = "10")
        private int waitingCount;

        public static Status of(TokenInfo.Status info) {
            return Status.builder()
                    .token(info.getToken())
                    .status(info.getStatus())
                    .waitingCount(info.getWaitingCount())
                    .build();
        }
    }

}
