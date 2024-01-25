package com.mapwithplan.mapplan.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("지도기반 일정 관리 프로젝트 API")
                        .description("일정 관리의 기능을 제공합니다.")
                        .version("1.0.0"));
    }

}
