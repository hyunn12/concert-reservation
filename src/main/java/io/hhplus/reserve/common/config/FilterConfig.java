package io.hhplus.reserve.common.config;

import io.hhplus.reserve.support.api.filter.TokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    FilterRegistrationBean<TokenFilter> tokenFilter() {
        FilterRegistrationBean<TokenFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new TokenFilter());

        registrationBean.addUrlPatterns(
                "/api/payment/**",
                "/api/reservation/**"
        );

        registrationBean.setOrder(1);

        registrationBean.setName("tokenFilter");

        return registrationBean;
    }

}
