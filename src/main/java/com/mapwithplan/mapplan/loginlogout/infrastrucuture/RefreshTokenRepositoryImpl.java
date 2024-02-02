package com.mapwithplan.mapplan.loginlogout.infrastrucuture;


import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.loginlogout.domain.RefreshToken;
import com.mapwithplan.mapplan.loginlogout.infrastrucuture.entity.RefreshTokenEntity;
import com.mapwithplan.mapplan.loginlogout.service.port.RefreshTokenRepository;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.infrastructure.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * RefreshTokenJPARepository 를 주입받아 구현한 클래스 입니다.
 * RefreshTokenRepository 인터페이스를 상속받아 모든 클래스를 구현합니다.
 * RefreshTokenEntity 와 관련된 모든 메서드를 구현합니다.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {


    private final RefreshTokenJPARepository refreshTokenJPARepository;

    /**
     * 회원에 대한 정보로 RefreshToken 을 DB 에서 조회합니다.
     * @param member 회원에 대한 정보가 들어 있습니다.
     * @return
     */
    @Override
    public Optional<RefreshToken> findByMember(Member member) {
        return refreshTokenJPARepository
                .findByMemberEntity(MemberEntity.from(member))
                .map(RefreshTokenEntity::toModel);
    }

    /**
     * 로그아웃에 사용되며 RefreshToken 을 DB 에서 삭제합니다.
     * @param refreshToken RefreshToken 에 대한 JWT 가 담겨 있습니다.
     */
    @Override
    public void delete(String refreshToken) {
        refreshTokenJPARepository.findByToken(refreshToken)
                .ifPresent(refreshTokenJPARepository::delete);
    }

    /**
     * 주로 로그인시에 사용되며 RefreshToken 을 DB 에 저장합니다.
     * @param refreshToken RefreshToken 를 생성하는 회원, 토큰 ID, RefreshToken JWT 값이 들어 있습니다.
     * @return
     */
    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenJPARepository.save(RefreshTokenEntity.from(refreshToken)).toModel();
    }

    /**
     * refreshToken JWT 값으로 DB 에서 토큰을 조회 합니다.
     * @param refreshToken
     * @return
     */
    @Override
    public RefreshToken findByToken(String refreshToken) {
        return refreshTokenJPARepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("RefreshToken",refreshToken))
                .toModel();

    }

    /**
     *
     * RefreshToken 이 만료되기 전 로그인 다시 하게 된다면,
     * DB 에 RefreshToken 을 새로 업데이트 합니다.
     * @param refreshToken RefreshToken 를 생성하는 회원, 토큰 ID, RefreshToken JWT 값이 들어 있습니다.
     */
    @Override
    public void update(RefreshToken refreshToken) {
        refreshTokenJPARepository
                .findByMemberEntity(MemberEntity.from(refreshToken.getMember()))
                .ifPresent(it -> it.updateToken(refreshToken.getToken()));

    }
}
