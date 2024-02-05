package com.mapwithplan.mapplan.jwt.util;

import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;


/**
 * 토큰을 생성하는 클래스입니다.
 * 만료 시간을 결정하며 JWT 토큰에 사용자에 대한 간단한 정보를 담습니다.
 */
@PropertySource("classpath:jwt.yml")
@Slf4j
@Component
public class JwtTokenizer {

    private final byte[] accessSecret;
    private final byte[] refreshSecret;


    public final static Long ACCESS_TOKEN_EXPIRE_COUNT = 30 * 60 * 1000L; // 30 minutes
    public final static Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L; // 7 days

    public JwtTokenizer(@Value("${secret-key}") String accessSecret,
                        @Value("${refresh-Key}") String refreshSecret) {
        this.accessSecret = accessSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshSecret = refreshSecret.getBytes(StandardCharsets.UTF_8);

    }

    /**
     * AccessToken 생성
     */
    public String createAccessToken(Long id, String email, List<String> roles, TimeClockHolder timeClockHolder) {
        return createToken(id, email, roles, ACCESS_TOKEN_EXPIRE_COUNT, accessSecret, timeClockHolder);
    }

    /**
     * RefreshToken 생성
     */
    public String createRefreshToken(Long id, String email, List<String> roles, TimeClockHolder timeClockHolder) {
        return createToken(id, email, roles, REFRESH_TOKEN_EXPIRE_COUNT, refreshSecret,timeClockHolder);
    }

    /**
     * 본격적인 토큰을 생성하는 메서드 입니다. 아래와 같은 파라미터로 생성을 진행합니다.
     * 만료시간은 각 토큰마다 다르게 설정합니다.
     * @param id
     * @param email
     * @param memberRoles
     * @param expire 토큰의 종류별로 다른 값을 현재 시간에 더해줍니다.
     * @param secretKey
     * @param timeClockHolder
     * @return
     */
    private String createToken(Long id, String email, List<String> memberRoles,
                               Long expire, byte[] secretKey, TimeClockHolder timeClockHolder) {
        return Jwts.builder()
                .subject(email)
                .claim("memberRoles",memberRoles)
                .claim("userId",id)
                .issuedAt(timeClockHolder.dateClockHold())
                .expiration(new Date(timeClockHolder.dateClockHold().getTime() + expire))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    /**
     * 토큰에서 유저의 아이디어를 얻는 메서드 입니다.
     */
    public Long getUserIdFromToken(String token) {
        String[] tokenArr = token.split(" ");
        token = tokenArr[1];
        Claims claims = parseToken(token, accessSecret);
        return Long.valueOf((Integer)claims.get("userId"));
    }

    /**
     * AccessToken 을 유요한지 검증합니다.
     * JwtAuthenticationProvider 에서 사용됩니다.
     * @param accessToken
     * @return
     */
    public Claims parseAccessToken(String accessToken) {
        return parseToken(accessToken, accessSecret);
    }

    /**
     * RefreshToken 을 검증합니다.
     * @param refreshToken
     * @return
     */
    public Claims parseRefreshToken(String refreshToken) {
        return parseToken(refreshToken, refreshSecret);
    }

    /**
     * 토큰을 분석합니다.
     * @param token
     * @param secretKey
     * @return
     */
    public Claims parseToken(String token, byte[] secretKey) {
        return Jwts.parser()
                .verifyWith(getSigningKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * HMAC-SHA 알고리즘을 사용해 SigningKey 를 얻습니다.
     * 각 토큰의 secretKey 를 사용합니다.
     * @param secretKey - byte형식
     * @return Key 형식 시크릿 키
     */
    public static SecretKey getSigningKey(byte[] secretKey) {
        return Keys.hmacShaKeyFor(secretKey);
    }


}
