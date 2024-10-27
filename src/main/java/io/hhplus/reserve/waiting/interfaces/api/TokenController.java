package io.hhplus.reserve.waiting.interfaces.api;

import io.hhplus.reserve.common.CommonConstant;
import io.hhplus.reserve.waiting.domain.TokenInfo;
import io.hhplus.reserve.waiting.domain.WaitingService;
import io.hhplus.reserve.waiting.interfaces.dto.TokenRequest;
import io.hhplus.reserve.waiting.interfaces.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/token")
@Tag(name = "Token", description = "Token 관련 API")
public class TokenController {

    private final WaitingService waitingService;

    public TokenController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/generate")
    @Operation(summary = "토큰 신규 발급", description = "토큰 신규 발급")
    public ResponseEntity<TokenResponse.Token> generateToken(
            @RequestBody TokenRequest.Generate request
    ) {

        TokenInfo.Token result = waitingService.generateToken(request.toCommand());

        return ResponseEntity.ok(TokenResponse.Token.of(result));
    }

    @PostMapping("/status" )
    @Operation(summary = "대기열 상태 조회", description = "현재 대기열 상태 조회 및 갱신")
    public ResponseEntity<TokenResponse.Status> getStatus(
            @RequestHeader(CommonConstant.TOKEN) String token
    ) {

        TokenRequest.Status request = TokenRequest.Status.builder().token(token).build();
        TokenInfo.Status result = waitingService.refreshToken(request.toCommand());

        return ResponseEntity.ok(TokenResponse.Status.of(result));
    }

}
