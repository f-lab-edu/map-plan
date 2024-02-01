package com.mapwithplan.mapplan.loginlogout.service.port;


import com.mapwithplan.mapplan.loginlogout.domain.RefreshToken;
import com.mapwithplan.mapplan.member.domain.Member;

import java.util.Optional;

public interface RefreshTokenRepository {


    Optional<RefreshToken> findByMember(Member member);

    void delete(String refreshToken);

    RefreshToken save(RefreshToken refreshToken);

    RefreshToken findByToken(String refreshToken);

    void update(RefreshToken refreshToken);
}
