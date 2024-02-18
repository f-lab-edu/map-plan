package com.mapwithplan.mapplan.loginlogout.service;

import com.mapwithplan.mapplan.loginlogout.domain.RefreshToken;
import com.mapwithplan.mapplan.loginlogout.service.port.RefreshTokenRepository;
import com.mapwithplan.mapplan.member.domain.Member;
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
     * saveRefreshToken 은 RefreshToken 을 DB 에 저장합니다.
     * @param refreshToken
     * @return
     */
    @Transactional
    public RefreshToken saveRefreshToken(RefreshToken refreshToken) {
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


    /**
     * 회원 정보로 refreshToken 을 찾습니다.
     * @param member 회원의 정보가 담긴 도메인 입니다.
     * @return RefreshToken 을 리턴합니다.
     */
    @Transactional
    public Optional<RefreshToken> findByMember(Member member){

        return refreshTokenRepository.findByMember(member);

    }

    /**
     * 다시 로그인 시 refreshToken 을 업데이트 합니다.
     * @param refreshToken 새로 생성된 토큰입니다.
     */
    @Transactional
    public void updateRefreshToken(RefreshToken refreshToken){
        refreshTokenRepository.update(refreshToken);
    }

    /**
     * 로그아웃을 진행하면 토큰을 기반으로 삭제를 진행합니다.
     * @param refreshToken
     */
    @Transactional
    public void deleteToken(String refreshToken){
        refreshTokenRepository.delete(refreshToken);
    }

}