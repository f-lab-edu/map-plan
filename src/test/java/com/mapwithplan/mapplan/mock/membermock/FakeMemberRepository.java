package com.mapwithplan.mapplan.mock.membermock;

import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.member.domain.MemberRole;
import com.mapwithplan.mapplan.member.domain.EditMember;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.service.port.MemberRepository;
import com.mapwithplan.mapplan.mock.TestClockProvider;


import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FakeMemberRepository implements MemberRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<Member> data = new ArrayList<>();
    @Override
    public Member saveMember(Member member) {
        if(member.getId() ==null || member.getId() == 0){
            Member newMember = Member.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .email(member.getEmail())
                    .name(member.getName())
                    .password(member.getPassword())
                    .memberStatus(member.getMemberStatus())
                    .memberRole(MemberRole.MEMBER)
                    .certificationCode(member.getCertificationCode())
                    .phone(member.getPhone())
                    .createdAt(member.getCreatedAt())
                    .modifiedAt(member.getCreatedAt())
                    .build();
            data.add(newMember);
            return newMember;
        } else{
            data.removeIf(test -> Objects.equals(test.getId(), member.getId()));
            data.add(member);
            return member;
        }
    }

    @Override
    public Optional<Member> findById(long id) {
        return data.stream().filter(test -> test.getId().equals(id)).findAny();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return data.stream().filter(item -> item.getEmail().equals(email)).findAny();

    }

    @Override
    public Member editMemberDetail(Member editMember) {

        EditMember editMember1 = new EditMember(editMember.getStatusMessage(), editMember.getPhone());
        Member member = data.stream()
                .filter(test -> test.getId().equals(editMember.getId()))
                .findAny().map(test ->
                        test.edit(test, editMember1, new TestClockProvider(3L))
                )
                .orElseThrow(() -> new ResourceNotFoundException("Member", editMember.getEmail()));

        return member;
    }

}
