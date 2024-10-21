package io.hhplus.reserve.support.api.interceptor;

import io.hhplus.reserve.waiting.domain.Waiting;
import io.hhplus.reserve.waiting.domain.WaitingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final WaitingService waitingService;

    public TokenInterceptor(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        if (isInvalidToken(token)) {
            log.error("#[TokenInterceptor] ::: Invalid token]");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        request.setAttribute("token", token);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean isInvalidToken(String token) {
        Waiting waiting = waitingService.validateToken(token);
        return waiting == null;
    }

}