package com.mapwithplan.mapplan.mock;

import com.mapwithplan.mapplan.member.domain.EMemberRole;
import com.mapwithplan.mapplan.member.domain.EMemberStatus;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.service.port.MemberRepository;

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
                    .eMemberRole(EMemberRole.MEMBER)
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
}
