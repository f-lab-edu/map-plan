package com.mapwithplan.mapplan.member.infrastructure;


import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.member.domain.EditMember;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.infrastructure.entity.MemberEntity;
import com.mapwithplan.mapplan.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {


    private final MemberJPARepository memberJPARepository;


    @Override
    public Member saveMember(Member member) {
        return memberJPARepository
                .save(MemberEntity.from(member))
                .toModel();
    }

    @Override
    public Optional<Member> findById(long id) {
        return memberJPARepository.findById(id).map(MemberEntity::toModel);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJPARepository.findByEmail(email).map(MemberEntity::toModel);

    }

    @Override
    public Member editMemberDetail(Member editMember) {
        MemberEntity memberEntity = memberJPARepository
                .findById(editMember.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Member", editMember.getEmail()));
        memberEntity.editFrom(editMember);
        return memberEntity.toModel();
    }
}
