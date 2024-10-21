package io.hhplus.reserve.common.config;

import io.hhplus.reserve.support.api.interceptor.LoggingInterceptor;
import io.hhplus.reserve.support.api.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;
    private final TokenInterceptor tokenInterceptor;

    public WebConfig(LoggingInterceptor loggingInterceptor, TokenInterceptor tokenInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
        this.tokenInterceptor = tokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/api/**");

        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns(
                        "/api/payment/**",
                        "/api/reservation/**",
                        "/api/token/status"
                );
    }

}
