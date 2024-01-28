package com.mapwithplan.mapplan.config.swagger;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 현재 기본 스웨거 화면에서는 토큰을 발급받아도 사용할 수가 없다.
 * 토큰을 헤더에 추가해서 사용하기 위해 SwaggerConfig의 OpenAPI 빈을 아래와 같이 수정한다.
 * API를 호출을 위해 HTTP 요청을 보낼 때
 * Authorization 헤더에 JWT 기반의 Bearer 토큰을 사용할 것이기 때문에 시큐리티 스키마는 위와 같이 설정하면 된다.
 */
@Configuration
public class SwaggerConfig {

    //SECURITY_SCHEME_NAME은 시큐리티 스키마의 이름을 뜻하기 때문에 원하는 이름을 사용할 수 있다.
    private static final String SECURITY_SCHEME_NAME = "authorization";	// 추가
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        //addSecuritySchemes()는 인증 정보 입력을 위한 버튼을
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                //addSecurityItem()은 시큐리티 요구 사항을 스웨거에 추가한다.
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .info(new Info()
                        .title("지도기반 일정 관리 프로젝트 API")
                        .description("일정 관리의 기능을 제공합니다.")
                        .version("1.0.0"));
    }

}
