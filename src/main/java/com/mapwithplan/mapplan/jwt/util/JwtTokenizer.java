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
     * 토큰에서 유저 아이디 얻기
     */
    public Long getUserIdFromToken(String token) {
        String[] tokenArr = token.split(" ");
        token = tokenArr[1];
        Claims claims = parseToken(token, accessSecret);
        return Long.valueOf((Integer)claims.get("userId"));
    }

    public Claims parseAccessToken(String accessToken) {
        return parseToken(accessToken, accessSecret);
    }

    public Claims parseRefreshToken(String refreshToken) {
        return parseToken(refreshToken, refreshSecret);
    }


    public Claims parseToken(String token, byte[] secretKey) {
        return Jwts.parser()
                .verifyWith(getSigningKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * @param secretKey - byte형식
     * @return Key 형식 시크릿 키
     */
    public static SecretKey getSigningKey(byte[] secretKey) {
        return Keys.hmacShaKeyFor(secretKey);
    }

}
