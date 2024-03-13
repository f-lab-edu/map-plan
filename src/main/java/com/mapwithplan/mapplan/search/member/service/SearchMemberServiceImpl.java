package com.mapwithplan.mapplan.search.member.service;


import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.search.member.controller.port.SearchMemberService;
import com.mapwithplan.mapplan.search.member.service.port.SearchMemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Builder
@RequiredArgsConstructor
@Service
public class SearchMemberServiceImpl implements SearchMemberService {
    private final SearchMemberRepository searchMemberRepository;

    /**
     * 이메일의 시작 부분이 일치하는 모든 회원의 정보를 가져옵니다.
     * 만약에, 조회된 회원이 없을시 size 가 0 인 리스트를 반환합니다.
     * @param email 조회하기 위한 이메일의 일부입니다.
     * @return 조회된 회원 리스트를 반환합니다.
     */
    @Override
    public final List<Member> findMemberByEmail(String email){

        return searchMemberRepository.findByEmailStartingWith(email);
    }

}
