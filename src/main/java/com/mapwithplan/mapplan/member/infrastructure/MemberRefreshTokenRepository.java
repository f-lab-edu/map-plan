package com.mapwithplan.mapplan.member.infrastructure;

import com.mapwithplan.mapplan.member.infrastructure.entity.MemberEntity;
import com.mapwithplan.mapplan.member.infrastructure.entity.MemberRefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 *
 */
public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshTokenEntity, UUID> {
    /**
     * 이 메소드는 소유자의 ID가 id면서 재발급 횟수가 count보다 작은 MemberRefreshToken 객체를 반환한다.
     * @param id 회원의 UUID
     * @param count 재발급한 횟수
     * @return
     */
    Optional<MemberRefreshTokenEntity> findByMemberIdAndReissueCountLessThan(UUID id,long count);

    Optional<MemberRefreshTokenEntity> findByMemberEntityId(long id);

}
