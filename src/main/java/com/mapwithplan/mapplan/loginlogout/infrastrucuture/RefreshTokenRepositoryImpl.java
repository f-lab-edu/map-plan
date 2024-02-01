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

@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {


    private final RefreshTokenJPARepository refreshTokenJPARepository;



    @Override
    public Optional<RefreshToken> findByMember(Member member) {
        return refreshTokenJPARepository
                .findByMemberEntity(MemberEntity.from(member))
                .map(RefreshTokenEntity::toModel);
    }

    @Override
    public void delete(String refreshToken) {
        refreshTokenJPARepository.findByToken(refreshToken)
                .ifPresent(refreshTokenJPARepository::delete);
    }


    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenJPARepository.save(RefreshTokenEntity.from(refreshToken)).toModel();
    }

    @Override
    public RefreshToken findByToken(String refreshToken) {
        return refreshTokenJPARepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("RefreshToken",refreshToken))
                .toModel();

    }

    @Override
    public void update(RefreshToken refreshToken) {
        refreshTokenJPARepository
                .findByMemberEntity(MemberEntity.from(refreshToken.getMember()))
                .ifPresent(it -> it.updateToken(refreshToken.getToken()));

    }
}
