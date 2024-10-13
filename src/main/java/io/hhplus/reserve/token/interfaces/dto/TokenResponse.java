package io.hhplus.reserve.token.interfaces.dto;

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

    }

}
