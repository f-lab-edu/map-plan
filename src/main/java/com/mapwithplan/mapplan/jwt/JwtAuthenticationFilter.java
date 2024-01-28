package com.mapwithplan.mapplan.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


/**
 * @Order를 통해 int범위 내에서 의존성 주입 우선순위를 설정한다(수치가 낮을수록 높다.).
 * 이 때 우선순위를 너무 높이면(= 값이 너무 작으면) 유효한 토큰이라도 인증이 안되고,
 * 우선순위가 너무 낮으면(= 값이 너무 크면) 토큰이 없어도 통과되기 때문에 적당한 값으로 설정한다.
 */
@Order(0)
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    /**
     * doFilterInternal()에서 인증 정보를 설정한다.
     * HTTP 요청 헤더의 토큰을 기반으로 생성한 User 객체를 토대로 스프링 시큐리티에서 사용할 UsernamePasswordAuthenticationToken 객체를 생성한다.
     * 이후 스프링 시큐리티 컨텍스트의 인증 정보를 새로 생성한 인증 토큰으로 설정하고 다음 필터로 넘어간다.
     * 인증 토큰의 details에는 요청을 날린 클라이언트 또는 프록시의 IP 주소와 세션 ID를 저장하는데,
     * 이 부분은 필요하지 않다면 삭제해도 된다.
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = parseBearerToken(request);
        User user = parseUserSpecification(token);
        AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken
                .authenticated(user, token, user.getAuthorities());
        authenticated.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticated);

        filterChain.doFilter(request, response);
    }

    /**
     * parseBearerToken()은 HTTP 요청의 헤더에서 Authorization값을 찾아서
     * Bearer로 시작하는지 확인하고 접두어를 제외한 토큰값으로 파싱한다.
     * 헤더에 Authorization이 존재하지 않거나 접두어가 Bearer가 아니면 null을 반환한다.
     * @param request
     * @return
     */
    private String parseBearerToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(token -> token.substring(0, 7).equalsIgnoreCase("Bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }

    /**
     * parseUserSpecification()은 토큰값을 토대로 토큰에 담긴 회원ID와 회원타입을 토대로
     * 스프링 시큐리티에서 사용할 User 객체를 반환한다.
     * 이 때 파싱된 토큰이 null이 아니면서 길이가 너무 짧지 않을 때만 토큰을 복호화하고,
     * 그 외에는 별도로 익명임을 뜻하는 User 객체를 생성한다. 비밀번호는 로그인 API를 호출할 때 이미 확인을 했기 때문에,
     * User 객체를 생성할 때는 사용하지 않으므로 빈 문자열을 넘긴다.
     * @param token
     * @return
     */
    private User parseUserSpecification(String token) {
        String[] split = Optional.ofNullable(token)
                .filter(subject -> subject.length() >= 10)
                .map(tokenProvider::validateTokenAndGetSubject)
                .orElse("anonymous:anonymous")
                .split(":");

        return new User(split[0], "", List.of(new SimpleGrantedAuthority(split[1])));
    }
}