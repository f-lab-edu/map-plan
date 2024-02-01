package com.mapwithplan.mapplan.loginlogout.infrastrucuture;


import com.mapwithplan.mapplan.loginlogout.infrastrucuture.entity.RefreshTokenEntity;
import com.mapwithplan.mapplan.member.infrastructure.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 *
 */
public interface RefreshTokenJPARepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

    Optional<RefreshTokenEntity> findByMemberEntity(MemberEntity memberEntity);

}
