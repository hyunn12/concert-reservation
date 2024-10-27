package io.hhplus.reserve.support.api.interceptor;

import io.hhplus.reserve.common.CommonConstant;
import io.hhplus.reserve.waiting.domain.TokenInfo;
import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingService;
import io.hhplus.reserve.waiting.domain.WaitingStatus;
import io.hhplus.reserve.waiting.interfaces.api.TokenController;
import io.hhplus.reserve.waiting.interfaces.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TokenInterceptorE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WaitingService waitingService;

    @MockBean
    private TokenController tokenController;

    private final String validToken = "valid_token";
    private final String invalidToken = "invalid_token";

    @BeforeEach
    void setup() {
        Waiting waiting = new Waiting(1L, 1L, 1L, validToken, WaitingStatus.WAIT);
        when(waitingService.validateToken(anyString())).thenReturn(waiting);

        TokenInfo.Status mockStatus = TokenInfo.Status.of(waiting, 5);
        when(waitingService.refreshToken(any())).thenReturn(mockStatus);

        TokenResponse.Status response = TokenResponse.Status.of(mockStatus);
        when(tokenController.getStatus(validToken)).thenReturn(ResponseEntity.ok(response));
    }

    @Test
    @DisplayName("정상 토큰으로 호출")
    void testTokenInterceptor() throws Exception {
        mockMvc.perform(post("/api/token/status")
                        .header(CommonConstant.TOKEN, validToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 호출 시 403 반환")
    void testTokenInterceptorWithInvalidToken() throws Exception {
        when(waitingService.validateToken(anyString())).thenReturn(null);

        mockMvc.perform(post("/api/token/status")
                        .header(CommonConstant.TOKEN, invalidToken))
                .andExpect(status().isForbidden());
    }
}
