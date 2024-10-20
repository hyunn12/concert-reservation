package io.hhplus.reserve.common.config;

import io.hhplus.reserve.common.interceptor.RestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RestInterceptor restInterceptor;

    public WebConfig(RestInterceptor restInterceptor) {
        this.restInterceptor = restInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(restInterceptor)
                .addPathPatterns("/api/**");
    }
}
