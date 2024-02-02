package com.mapwithplan.mapplan.loginlogout.service;

import com.mapwithplan.mapplan.loginlogout.domain.RefreshToken;
import com.mapwithplan.mapplan.loginlogout.service.port.RefreshTokenRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * RefreshTokenService 는 RefreshTokenRepository 을 주입 받아 사용한 클래스 입니다.
 *
 */
@Builder
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * addRefreshToken 은 RefreshToken 을 DB 에 저장합니다.
     * @param refreshToken
     * @return
     */
    @Transactional
    public RefreshToken addRefreshToken(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * refreshToken 값을 이용해 토큰을 조회합니다.
     * @param refreshToken
     * @return
     */
    @Transactional(readOnly = true)
    public RefreshToken findRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken);
    }
}