package com.mapwithplan.mapplan.search.member.infrastructure;

import com.mapwithplan.mapplan.member.infrastructure.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchMemberJPARepository extends JpaRepository<MemberEntity,Long> {


    List<MemberEntity> findByEmailStartingWith(String email);
}
