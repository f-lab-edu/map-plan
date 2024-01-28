package com.mapwithplan.mapplan.jwt;

import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * JWT 에 대한 토큰을 생성하는 Provider 입니다.
 * 암호 알고리즘으로는 HS512 를 사용하고 있습니다.
 */
@PropertySource("classpath:jwt.yml")
@Service
public class TokenProvider {
    private final SecretKey secretKey;
    private final long expirationHours;
    private final String issuer;

    /**
     * yml에 등록해둔 값을 불러오면서 SecretKey를 생성한다.
     * @param secret JWT의 서명에 사용할 비밀키. 서명부의 암호화 알고리즘으로 HS512를 사용할 것이기 때문에 길이가 512비트(64바이트) 이상인 비밀키를 사용해야 한다.
     * @param expirationHours 만료 시간
     * @param issuer 토큰 발급자
     */
    public TokenProvider(@Value("${secret-key}") String secret,
                         @Value("${expiration-hours}") long expirationHours,
                         @Value("${issuer}") String issuer) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),Jwts.SIG.HS512.key().build().getAlgorithm());
        this.expirationHours = expirationHours;
        this.issuer = issuer;
    }

    /**
     * 토큰을 생성한다.
     * @param userSpecification JWT 토큰 제목
     * @return
     */
    public String createToken(String userSpecification) {
        return Jwts.builder()
                .signWith(secretKey)   // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
                .subject(userSpecification)  // JWT 토큰 제목
                .issuer(issuer)  // JWT 토큰 발급자
                .issuedAt(Timestamp.valueOf(LocalDateTime.now()))    // JWT 토큰 발급 시간
                .expiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS)))    // JWT 토큰 만료 시간
                .compact(); // JWT 토큰 생성
    }

    /**
     * validateTokenAndGetSubject()는 비밀키를 토대로 createToken()에서 토큰에 담은 Subject를
     * 복호화하여 문자열 형태로 반환하는 메소드이다.
     * 그리고 이 Subject에는 MemberService의 logIn()에서 토큰을 생성할 때
     * 인자로 넘긴 "{회원ID}:{회원타입}"이 담겨있다.
     * @param token
     * @return
     */
    public String validateTokenAndGetSubject(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


}
