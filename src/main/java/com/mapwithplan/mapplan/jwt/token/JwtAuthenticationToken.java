package com.mapwithplan.mapplan.jwt.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 토큰에 대한 정보를 담는 역할을 합니다. 주로 필터와 토큰 생성시 사용됩니다.
 */
@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private String token;
    private Object principal;
    private Object credentials;

    /**
     * 제공된 권한 배열을 사용하여 토큰을 만듭니다.
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities,
                                  Object principal, Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(true);
    }

    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
        this.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.credentials;
    }
}