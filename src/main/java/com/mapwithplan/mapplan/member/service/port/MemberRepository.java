package com.mapwithplan.mapplan.member.service.port;

import com.mapwithplan.mapplan.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    Member saveMember(Member member);

    Optional<Member> findById(long id);

    Optional<Member> findByEmail(String email);

}
