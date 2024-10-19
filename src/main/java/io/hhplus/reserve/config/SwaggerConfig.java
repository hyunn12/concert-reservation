package io.hhplus.reserve.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "콘서트 예약 서비스",
                description = "항해 플러스 서버구축 - 콘서트 예약 서비스",
                version = "v1.0"))
@Configuration
public class SwaggerConfig {
}

