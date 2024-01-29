package com.mapwithplan.mapplan.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * 이 엔트리포인트는 스프링 시큐리티에서 인증과 관련된 예외가 발생했을 때 이를 처리하는 로직을 담당합니다.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;
    /**
     * 여기서 HandlerExceptionResolver 의 빈을 주입받고 있는데,
     *  HandlerExceptionResolver 의 빈이 두 종류가 있기 때문에
     *  @Qualifier 로 handlerExceptionResolver 를 주입받을 것이라고 명시해야 합니다.
     * @param resolver
     */
    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * commence()에서 스프링 시큐리티의 인증 관련 예외를 처리하게 됩니다.
     * @ControllerAdvice 에서 모든 예외를 처리하여 응답할 것이기 때문에 여기에 별다른 로직은 작성하지 않고 HandlerExceptionResolver 에 예외 처리를 위임한다.
     * resolveException()의 인자로 넘겨주던 예외를 AuthenticationException 대신 request에서 가져온 예외로 변경해 줬다.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        resolver.resolveException(request, response, null, (Exception) request.getAttribute("exception"));
    }
}
