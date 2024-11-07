package io.hhplus.reserve.common.config;

import io.hhplus.reserve.support.api.interceptor.LoggingInterceptor;
import io.hhplus.reserve.support.api.interceptor.TokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;
    private final TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/api/**");

        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns(
                        "/api/payment/**",
                        "/api/reservation/**"
                );
    }

}
