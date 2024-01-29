package com.mapwithplan.mapplan.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapwithplan.mapplan.member.infrastructure.MemberRefreshTokenRepository;
import com.mapwithplan.mapplan.member.infrastructure.entity.MemberRefreshTokenEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * JWT 에 대한 토큰을 생성하는 Provider 입니다.
 * 암호 알고리즘으로는 HS512 를 사용하고 있습니다.
 */
@PropertySource("classpath:jwt.yml")
@Service
public class TokenProvider {
    private final SecretKey secretKey;
    private final long expirationMinutes;	// hours -> minutes
    private final long refreshExpirationHours;
    private final String issuer;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final long reissueLimit;
    private final ObjectMapper objectMapper = new ObjectMapper();	// JWT 역직렬화를 위한 ObjectMapper
    /**
     * yml에 등록해둔 값을 불러오면서 SecretKey 를 생성한다.
     * @param secret JWT 의 서명에 사용할 비밀키. 서명부의 암호화 알고리즘으로 HS512를 사용할 것이기 때문에 길이가 512비트(64바이트) 이상인 비밀키를 사용해야 한다.
     * @param expirationMinutes 만료 분
     * @param refreshExpirationHours 리프레시 토큰 시간
     * @param issuer 토큰 발급자
     */
    public TokenProvider(@Value("${secret-key}") String secret,
                         @Value("${expiration-minutes}") long expirationMinutes,
                         @Value("${refresh-expiration-hours}") long refreshExpirationHours,
                         @Value("${issuer}") String issuer,
                         MemberRefreshTokenRepository memberRefreshTokenRepository) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),Jwts.SIG.HS512.key().build().getAlgorithm());
        this.expirationMinutes = expirationMinutes;
        this.refreshExpirationHours = refreshExpirationHours;
        this.issuer = issuer;
        this.memberRefreshTokenRepository = memberRefreshTokenRepository;
        this.reissueLimit = refreshExpirationHours * 60 / expirationMinutes;	// 재발급 한도
    }

    /**
     * 토큰을 생성한다.
     * @param userSpecification JWT 토큰 제목
     * @return
     */
    public String createAccessToken(String userSpecification) {
        return Jwts.builder()
                .signWith(secretKey)   // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
                .subject(userSpecification)  // JWT 토큰 제목
                .issuer(issuer)  // JWT 토큰 발급자
                .issuedAt(Timestamp.valueOf(LocalDateTime.now()))    // JWT 토큰 발급 시간
                .expiration(Date.from(Instant.now().plus(expirationMinutes, ChronoUnit.HOURS)))    // JWT 토큰 만료 시간
                .compact(); // JWT 토큰 생성
    }
    public String createRefreshToken() {
        return Jwts.builder()
                .signWith(secretKey)
                .issuer(issuer)
                .issuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .expiration(Date.from(Instant.now().plus(refreshExpirationHours, ChronoUnit.HOURS)))
                .compact();
    }
    /**
     * validateTokenAndGetSubject()는 비밀키를 토대로 createToken()에서 토큰에 담은 Subject를
     * 복호화하여 문자열 형태로 반환하는 메소드이다.
     * 그리고 이 Subject에는 MemberService의 logIn()에서 토큰을 생성할 때
     * 인자로 넘긴 "{회원ID}:{회원타입}"이 담겨있다.
     * @param token 토큰을 확인한다.
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

    /**
     * recreateAccessToken()은 기존 액세스 토큰을 토대로 새로운 액세스 토큰을 생성한다.
     * MemberRefreshTokenRepository 에서 기존 액세스 토큰에 담긴 회원ID의,
     * 재발급 한도에 도달하지 않은 리프레시 토큰을 찾아내서 재발급 횟수를 +1하고 새로운 액세스 토큰을 반환한다.
     * 이 과정에서 해당 조건의 리프레시 토큰을 발견하지 못하면 강제로 ExpiredJwtException을 발생시킨다.
     * JPA의 영속성 컨텍스트를 사용하기 때문에 @Transactional 어노테이션을 붙여줘야 한다.
     * @param oldAccessToken
     * @return
     * @throws JsonProcessingException
     */
    @Transactional
    public String recreateAccessToken(String oldAccessToken) throws JsonProcessingException {
        String subject = decodeJwtPayloadSubject(oldAccessToken);
        memberRefreshTokenRepository.findByMemberIdAndReissueCountLessThan(UUID.fromString(subject.split(":")[0]), reissueLimit)
                .ifPresentOrElse(
                        MemberRefreshTokenEntity::increaseReissueCount,
                        () -> { throw new ExpiredJwtException(null, null, "Refresh token expired."); }
                );
        return createAccessToken(subject);
    }

    /**
     * validateRefreshToken()은 리프레시 토큰이 유효한 토큰인지를 검증한다.
     * validateAndParseToken()을 호출하여 리프레시 토큰 자체가 유효한 토큰인지를 검사한다.
     * 리프레시 토큰 자체가 유효한 토큰이라면 기존 액세스 토큰에 담긴 회원ID의,
     * 재발급 한도에 도달하지 않은 리프레시 토큰을 찾아서 인자로 받은 리프레시 토큰과 비교한다.
     * 해당하는 리프레시 토큰이 없으면 강제로 ExpiredJwtException 을 발생시켜서 만료된 토큰으로 취급한다.
     * @param refreshToken
     * @param oldAccessToken
     * @throws JsonProcessingException
     */
    @Transactional(readOnly = true)
    public void validateRefreshToken(String refreshToken, String oldAccessToken) throws JsonProcessingException {
        validateAndParseToken(refreshToken);
        String memberId = decodeJwtPayloadSubject(oldAccessToken).split(":")[0];
        memberRefreshTokenRepository.findByMemberIdAndReissueCountLessThan(UUID.fromString(memberId), reissueLimit)
                .filter(memberRefreshToken -> memberRefreshToken.validateRefreshToken(refreshToken))
                .orElseThrow(() -> new ExpiredJwtException(null, null, "Refresh token expired."));
    }

    /**
     * 내부의 .verifyWith(secretKey)에서 JWT를 파싱할 때, 토큰이 유효한지 검사하여 예외를 던지기 때문에 토큰 검증에 사용할 수 있다.
     * @param token
     * @return
     */
    private Jws<Claims> validateAndParseToken(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }

    /**
     * JWT를 복호화하고 데이터가 담겨있는 Payload에서 Subject를 반환한다.
     * 이 Subject에는 회원ID와 회원 타입이 문자열 형태로 담겨있다.
     * 이 메소드는 이미 만료된 액세스 토큰을 복호화할 것이기 때문에 유효한 토큰인지는 검사하지 않는다.
     * 유효시간 만료가 아닌 이유로 무효한 토큰이라면 해당 메소드를 호출하지 않을 것이다.
     * @param oldAccessToken
     * @return
     * @throws JsonProcessingException
     */
    private String decodeJwtPayloadSubject(String oldAccessToken) throws JsonProcessingException {
        return objectMapper.readValue(
                new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]), StandardCharsets.UTF_8),
                Map.class
        ).get("sub").toString();
    }
}
