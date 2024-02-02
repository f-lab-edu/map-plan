package com.mapwithplan.mapplan.jwt.provider;

import com.mapwithplan.mapplan.jwt.token.JwtAuthenticationToken;
import com.mapwithplan.mapplan.jwt.util.JwtTokenizer;
import io.jsonwebtoken.Claims;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 본격적인 토튼에 대한 검증을 진행하는 Provider 입니다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenizer jwtTokenizer;

    /**
     * 토큰을 검증하고 기간이 만료되었는지, 토큰 문자열이 문제가 있는지 등 Exception이 발생합니다.
     * sub에 암호화된 데이터를 집어넣고, 복호화하는 코드를 넣어줄 수 있다.
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;

        Claims claims = jwtTokenizer.parseAccessToken(authenticationToken.getToken());
        String email = claims.getSubject();
        List<GrantedAuthority> authorities = getGrantedAuthorities(claims);

        return new JwtAuthenticationToken(authorities, email, null);
    }

    /**
     * 회원의 역할 ADMIN, MEMBER 를 찾아 authorities 를 리턴합니다.
     * @param claims
     * @return authorities
     */
    private List<GrantedAuthority> getGrantedAuthorities(Claims claims) {
        List<String> roles = (List<String>) claims.get("memberRoles");
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(()-> role);
        }
        return authorities;
    }

    /**
     * 유효한 폼을 가지고 있는지 검증하는 메서드 입니다.
     * @param authentication boolean을 리턴합니다.
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}