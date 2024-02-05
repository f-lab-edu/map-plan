package com.mapwithplan.mapplan.jwt.util;

import com.mapwithplan.mapplan.mock.TestClockHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


class JwtTokenizerTest {

    JwtTokenizer jwtTokenizer;
    final Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L;
    final Long ACCESS_TOKEN_EXPIRE_COUNT = 30 * 60 * 1000L;
    String accessSecret= "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest";
    String refreshToken= "abcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefg";

    String email = "testAOP@gmail.com";
    List<String> roles = List.of("MEMBER");
    Long id = 1L;
    TestClockHolder testClockHolder;

    @BeforeEach
    void init(){
        this.jwtTokenizer = new JwtTokenizer(accessSecret, refreshToken);

    }

    @Test
    @DisplayName("createAccessToken 토큰을 생성한다.")
    void createAccessTokenTest() {
        //Given
        this.testClockHolder = new TestClockHolder(9999999999999999L);
        byte[] accessSecretBytes = this.accessSecret.getBytes(StandardCharsets.UTF_8);
        //When
        String JwtToken = Jwts.builder()
                .subject(email)
                .claim("memberRoles",roles)
                .claim("userId",id)
                .issuedAt(testClockHolder.dateClockHold())
                .expiration(new Date(testClockHolder.dateClockHold().getTime() + this.ACCESS_TOKEN_EXPIRE_COUNT))
                .signWith(JwtTokenizer.getSigningKey(accessSecretBytes))
                .compact();
        //Then
        String accessToken = jwtTokenizer.createAccessToken(id, email, roles, testClockHolder);


        assertThat(JwtToken).isEqualTo(accessToken);

    }
    @Test
    @DisplayName("createRefreshToken 토큰을 생성한다.")
    void createRefreshTokenTest() {
        //Given
        this.testClockHolder = new TestClockHolder(9999999999999999L);
        byte[] refreshTokenBytes = this.refreshToken.getBytes(StandardCharsets.UTF_8);

        /**
         * 토큰을 만들때 순서도 토큰에 영향을 준다 참고하자.
         */
        //When
        String JwtToken = Jwts.builder()
                .subject(email)
                .claim("memberRoles",roles)
                .claim("userId",id)
                .issuedAt(testClockHolder.dateClockHold())
                .expiration(new Date(testClockHolder.dateClockHold().getTime() + this.REFRESH_TOKEN_EXPIRE_COUNT))
                .signWith(JwtTokenizer.getSigningKey(refreshTokenBytes))
                .compact();
        //Then
        String refreshToken = jwtTokenizer.createRefreshToken(id, email, roles, testClockHolder);


        assertThat(JwtToken).isEqualTo(refreshToken);

    }

    @Test
    @DisplayName("parseAccessTokenTest 테스트로 만료토큰을 분석한다.")
    void parseAccessExpiredTokenTest() {
        this.testClockHolder = new TestClockHolder(999999999L);
        //Given
        byte[] accessSecretBytes = this.accessSecret.getBytes(StandardCharsets.UTF_8);
        String accessToken = Jwts.builder()
                .subject(email)
                .claim("memberRoles",roles)
                .claim("userId",id)
                .issuedAt(testClockHolder.dateClockHold())
                .expiration(new Date(testClockHolder.dateClockHold().getTime() + this.ACCESS_TOKEN_EXPIRE_COUNT))
                .signWith(JwtTokenizer.getSigningKey(accessSecretBytes))
                .compact();
        //When
        assertThatThrownBy(()->jwtTokenizer.parseAccessToken(accessToken))
                .isInstanceOf(ExpiredJwtException.class);
        //Then
    }

    @Test
    @DisplayName("parseAccessTokenTest 테스트로 만료하지 않은 토큰을 분석한다.")
    void parseAccessNotExpiredTokenTest() {
        this.testClockHolder = new TestClockHolder(900000000000000L);
        //Given
        byte[] accessSecretBytes = this.accessSecret.getBytes(StandardCharsets.UTF_8);
        String accessToken = Jwts.builder()
                .subject(email)
                .claim("memberRoles",roles)
                .claim("userId",id)
                .issuedAt(testClockHolder.dateClockHold())
                .expiration(new Date(testClockHolder.dateClockHold().getTime() + this.ACCESS_TOKEN_EXPIRE_COUNT))
                .signWith(JwtTokenizer.getSigningKey(accessSecretBytes))
                .compact();
        //When
        Claims claims = jwtTokenizer.parseAccessToken(accessToken);
        //Then
        assertThat(claims.get("memberRoles")).isEqualTo(roles);
        assertThat(claims.get("userId")).isEqualTo(1);
        assertThat(claims.getIssuedAt()).isEqualTo(this.testClockHolder.dateClockHold());
        assertThat(claims.getExpiration()).isEqualTo(new Date(this.testClockHolder.dateClockHold().getTime()+ this.ACCESS_TOKEN_EXPIRE_COUNT));

    }


    @Test
    @DisplayName("parseRefreshToken 테스트로 만료하지 않은 토큰을 분석한다.")
    void parseRefreshTokenTest() {
        this.testClockHolder = new TestClockHolder(900000000000000L);
        //Given
        byte[] refreshTokenBytes = this.refreshToken.getBytes(StandardCharsets.UTF_8);
        String refreshToken = Jwts.builder()
                .subject(email)
                .claim("memberRoles",roles)
                .claim("userId",id)
                .issuedAt(testClockHolder.dateClockHold())
                .expiration(new Date(testClockHolder.dateClockHold().getTime() + this.REFRESH_TOKEN_EXPIRE_COUNT))
                .signWith(JwtTokenizer.getSigningKey(refreshTokenBytes))
                .compact();
        //When
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
        //Then
        assertThat(claims.get("memberRoles")).isEqualTo(roles);
        assertThat(claims.get("userId")).isEqualTo(1);
        assertThat(claims.getIssuedAt()).isEqualTo(this.testClockHolder.dateClockHold());
        assertThat(claims.getExpiration()).isEqualTo(new Date(this.testClockHolder.dateClockHold().getTime()+ this.REFRESH_TOKEN_EXPIRE_COUNT));

    }

    @Test
    @DisplayName("parseRefreshToken 테스트로 만료된 토큰을 분석한다.")
    void parseRefreshTokenExpireTest() {
        this.testClockHolder = new TestClockHolder(999999999L);
        //Given
        byte[] accessSecretBytes = this.refreshToken.getBytes(StandardCharsets.UTF_8);
        String refreshToken = Jwts.builder()
                .subject(email)
                .claim("memberRoles",roles)
                .claim("userId",id)
                .issuedAt(testClockHolder.dateClockHold())
                .expiration(new Date(testClockHolder.dateClockHold().getTime() + this.REFRESH_TOKEN_EXPIRE_COUNT))
                .signWith(JwtTokenizer.getSigningKey(accessSecretBytes))
                .compact();
        //When

        //Then
        assertThatThrownBy(()->jwtTokenizer.parseRefreshToken(refreshToken))
                .isInstanceOf(ExpiredJwtException.class);
    }


    @Test
    @DisplayName("getUserIdFromToken 로 유저의 아이디를 얻을 수 있다.")
    void getUserIdFromTokenTest() {
        //Given
        this.testClockHolder = new TestClockHolder(900000000000000L);
        byte[] accessSecretBytes = this.accessSecret.getBytes(StandardCharsets.UTF_8);
        String accessToken = Jwts.builder()
                .subject(email)
                .claim("memberRoles",roles)
                .claim("userId",id)
                .issuedAt(testClockHolder.dateClockHold())
                .expiration(new Date(testClockHolder.dateClockHold().getTime() + this.ACCESS_TOKEN_EXPIRE_COUNT))
                .signWith(JwtTokenizer.getSigningKey(accessSecretBytes))
                .compact();
        //When
        Long userIdFromToken = jwtTokenizer.getUserIdFromToken("Bearer "+accessToken);
        //Then

        assertThat(userIdFromToken).isEqualTo(1);
    }
}