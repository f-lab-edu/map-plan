package com.mapwithplan.mapplan.member.service.port;

import com.mapwithplan.mapplan.member.domain.EditMember;
import com.mapwithplan.mapplan.member.domain.Member;

import java.util.Optional;

/**
 * MemberRepository 는 MemberServiceImpl 과 함께 사용하는 Port 역할을 합니다.
 */
public interface MemberRepository {

    Member saveMember(Member member);

    Optional<Member> findById(long id);

    Optional<Member> findByEmail(String email);

    Member editMemberDetail(Member editMember);

}
