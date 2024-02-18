package com.mapwithplan.mapplan.loginlogout.infrastrucuture;


import com.mapwithplan.mapplan.loginlogout.infrastrucuture.entity.RefreshTokenEntity;
import com.mapwithplan.mapplan.member.infrastructure.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JpaRepository 를 사용하는 RefreshTokenJPARepository 입니다.
 * 메서드에 대한 설명은 RefreshTokenRepositoryImpl 를 참고하시길 바랍니다.
 */
public interface RefreshTokenJPARepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

    Optional<RefreshTokenEntity> findByMemberEntity(MemberEntity memberEntity);

}
