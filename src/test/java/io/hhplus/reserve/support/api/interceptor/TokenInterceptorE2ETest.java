package io.hhplus.reserve.support.api.interceptor;

import io.hhplus.reserve.TestContainerSupport;
import io.hhplus.reserve.common.CommonConstant;
import io.hhplus.reserve.waiting.domain.WaitingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TokenInterceptorE2ETest extends TestContainerSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WaitingService waitingService;

    private final String validToken = "valid_token";
    private final String invalidToken = "invalid_token";

    @BeforeEach
    void setup() {
        // todo 임시 redis 추가
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
        when(waitingService.checkActiveToken(anyString())).thenReturn(null);

        mockMvc.perform(post("/api/token/status")
                        .header(CommonConstant.TOKEN, invalidToken))
                .andExpect(status().isForbidden());
    }
}
