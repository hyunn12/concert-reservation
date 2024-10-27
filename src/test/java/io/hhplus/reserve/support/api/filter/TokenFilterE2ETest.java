package io.hhplus.reserve.support.api.filter;

import io.hhplus.reserve.common.CommonConstant;
import io.hhplus.reserve.payment.application.PaymentFacade;
import io.hhplus.reserve.waiting.domain.TokenInfo;
import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingService;
import io.hhplus.reserve.waiting.domain.WaitingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TokenFilterE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentFacade paymentFacade;

    @MockBean
    private WaitingService waitingService;

    @BeforeEach
    void setup() {
        Waiting waiting = new Waiting(1L, 1L, 1L, "valid_token", WaitingStatus.WAIT);
        when(waitingService.validateToken(anyString())).thenReturn(waiting);

        TokenInfo.Status mockStatus = TokenInfo.Status.of(waiting, 5);
        when(waitingService.refreshToken(any())).thenReturn(mockStatus);
    }

    @Test
    @DisplayName("토큰 없이 호출 시 401 반환")
    void testFilterWithoutToken() throws Exception {
        mockMvc.perform(post("/api/token/status"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("빈 토큰값으로 호출 시 401 반환")
    void testFilterWithEmptyToken() throws Exception {
        mockMvc.perform(post("/api/token/status")
                        .header(CommonConstant.TOKEN, ""))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("정상 토큰으로 호출")
    void testFilter() throws Exception {
        mockMvc.perform(post("/api/token/status")
                        .header(CommonConstant.TOKEN, "valid_token"))
                .andExpect(status().isOk());
    }
}