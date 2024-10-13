package io.hhplus.reserve.token.interfaces.api;

import io.hhplus.reserve.token.interfaces.dto.TokenRequest;
import io.hhplus.reserve.token.interfaces.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
@Tag(name = "Token", description = "Token 관련 API")
public class TokenController {

    // 토큰 발급
    // 토큰 상태 조회 = 대기 상태 조회 (polling) == 갱신

    @PostMapping("/generate")
    @Operation(summary = "토큰 신규 발급", description = "토큰 신규 발급")
    public TokenResponse.Token generateToken(
            @RequestBody TokenRequest.Generate request
    ) {
        // TODO 토큰 발급 API 작성

        return TokenResponse.Token.builder()
                .token("testtokentokentoken")
                .status("WAIT")
                .build();
    }

    @PostMapping("/status" )
    @Operation(summary = "토큰 상태 조회", description = "현재 대기열 상태 조회 및 갱신")
    public TokenResponse.Status getStatus(
            @RequestBody TokenRequest.Status request
    ) {
        // TODO 대기열 상태 조회 API 작성

        return TokenResponse.Status.builder()
                .token(request.getToken())
                .status("WAIT")
                .waitingCount(10)
                .build();
    }

}
