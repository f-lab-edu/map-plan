package com.mapwithplan.mapplan.member.infrastructure;

import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.infrastructure.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJPARepository extends JpaRepository<MemberEntity,Long>  {

    Optional<MemberEntity> findByEmail(String email);
}
