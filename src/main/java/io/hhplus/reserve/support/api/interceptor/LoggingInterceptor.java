package io.hhplus.reserve.support.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String CURRENT_TIME = "__current_time__";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(CURRENT_TIME, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        if (request.getAttribute(CURRENT_TIME) != null) {
            log.info("ip : {}, method : {}, url : {}, params : {}, status : {}, actual time : {}(ms)",
                    request.getRemoteAddr(),
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getQueryString(),
                    response.getStatus(),
                    System.currentTimeMillis() - (long) request.getAttribute(CURRENT_TIME));
        }
    }
}


