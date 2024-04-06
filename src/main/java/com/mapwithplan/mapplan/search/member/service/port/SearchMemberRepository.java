package com.mapwithplan.mapplan.search.member.service.port;

import com.mapwithplan.mapplan.member.domain.Member;

import java.util.List;

public interface SearchMemberRepository {

    List<Member> findByEmailStartingWith(String email);
}
