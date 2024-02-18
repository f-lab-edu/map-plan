package com.mapwithplan.mapplan.loginlogout.service.port;


import com.mapwithplan.mapplan.loginlogout.domain.RefreshToken;
import com.mapwithplan.mapplan.member.domain.Member;

import java.util.Optional;

/**
 * Port 역할을 하며
 * RefreshTokenService 와 의존 관계를 맺는 인터페이스 입니다.
 * RefreshTokenRepositoryImpl 에 모든 메서드가 구현 되어 있습니다.
 */
public interface RefreshTokenRepository {


    Optional<RefreshToken> findByMember(Member member);

    void delete(String refreshToken);

    RefreshToken save(RefreshToken refreshToken);

    RefreshToken findByToken(String refreshToken);

    void update(RefreshToken refreshToken);
}
