package io.hhplus.reserve.waiting.interfaces.api;

import io.hhplus.reserve.common.CommonConstant;
import io.hhplus.reserve.waiting.domain.TokenInfo;
import io.hhplus.reserve.waiting.domain.WaitingService;
import io.hhplus.reserve.waiting.interfaces.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
@Tag(name = "Token", description = "Token 관련 API")
public class TokenController {

    private final WaitingService waitingService;

    public TokenController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @GetMapping("/check")
    @Operation(summary = "대기열 토큰 발급/조회", description = "현재 대기열 상태 조회 및 발급")
    public ResponseEntity<TokenResponse.Token> checkToken(
            @Schema(description = "대기열 토큰")
            @RequestHeader(CommonConstant.TOKEN) String token
    ) {
        TokenInfo.Main result = waitingService.checkToken(token);
        return ResponseEntity.ok(TokenResponse.Token.of(result));
    }

}
