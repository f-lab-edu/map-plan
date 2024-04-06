package com.mapwithplan.mapplan.mock.searchmembermock;

import com.mapwithplan.mapplan.member.domain.Member;

import com.mapwithplan.mapplan.member.service.port.MemberRepository;
import com.mapwithplan.mapplan.mock.membermock.FakeMemberRepository;
import com.mapwithplan.mapplan.search.member.service.port.SearchMemberRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FakeSearchMemberRepository implements SearchMemberRepository {

    private final FakeMemberRepository fakeMemberRepository;
    @Override
    public List<Member> findByEmailStartingWith(String email) {

        List<Member> allMember = fakeMemberRepository.getAllMember();

        return allMember
                .stream()
                .filter(item -> item.getEmail().startsWith(email))
                .toList();
    }


}
