package com.mapwithplan.mapplan.search.member.controller.port;

import com.mapwithplan.mapplan.member.domain.Member;

import java.util.List;

public interface SearchMemberService {

    List<Member> findMemberByEmail(String email);
}
