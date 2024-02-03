package com.mapwithplan.mapplan.member.infrastructure;

import com.mapwithplan.mapplan.member.infrastructure.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJPARepository extends JpaRepository<MemberEntity,Long>  {
}
