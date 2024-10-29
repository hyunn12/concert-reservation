package io.hhplus.reserve.support.api.filter;

import io.hhplus.reserve.TestContainerSupport;
import io.hhplus.reserve.common.CommonConstant;
import io.hhplus.reserve.waiting.domain.TokenInfo;
import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingStatus;
import io.hhplus.reserve.waiting.interfaces.api.TokenController;
import io.hhplus.reserve.waiting.interfaces.dto.TokenResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TokenFilterE2ETest extends TestContainerSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenController tokenController;

    private final String validToken = "valid_token";

    @BeforeEach
    void setUp() {
        // TokenFilter 만 사용하도록 설정
        this.mockMvc = MockMvcBuilders.standaloneSetup(tokenController)
                .addFilters(new TokenFilter())
                .build();

        Waiting waiting = new Waiting(1L, 1L, 1L, "valid_token", WaitingStatus.WAIT);
        TokenInfo.Status mockStatus = TokenInfo.Status.of(waiting, 5);
        TokenResponse.Status response = TokenResponse.Status.of(mockStatus);
        when(tokenController.getStatus(validToken)).thenReturn(ResponseEntity.ok(response));
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
                        .header(CommonConstant.TOKEN, validToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}