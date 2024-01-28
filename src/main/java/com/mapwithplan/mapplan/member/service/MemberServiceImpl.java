package com.mapwithplan.mapplan.member.service;


import com.mapwithplan.mapplan.common.exception.DuplicateResourceException;
import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.common.timeutils.service.port.LocalDateTimeClockHolder;
import com.mapwithplan.mapplan.common.uuidutils.service.port.UuidHolder;
import com.mapwithplan.mapplan.member.controller.port.MemberService;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.domain.MemberCreate;
import com.mapwithplan.mapplan.member.service.port.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Builder
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final CertificationService certificationService;

    private final LocalDateTimeClockHolder clockHolder;

    private final UuidHolder uuidHolder;

    private final PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public Member saveMember(MemberCreate memberCreate) {
        
        // 비밀 번호 암호화 후 저장
        Member member = Member.from(memberCreate, clockHolder,uuidHolder,passwordEncoder);
        Optional<Member> findByEmailMember = memberRepository.findByEmail(memberCreate.getEmail());
        if(findByEmailMember.isPresent()){
            throw new DuplicateResourceException(memberCreate.getEmail());
        }

        member = memberRepository.saveMember(member);
        certificationService.send(memberCreate.getEmail(),member.getId(),member.getCertificationCode());
        return member;
    }

    @Transactional
    @Override
    public void verifyEmail(long id, String certificationCode) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member", id));
        log.info("member= {}",member.getEMemberType());
        member = member.certificate(certificationCode);
        log.info("member= {}",member.getEMemberType());
        memberRepository.saveMember(member);
    }

    @Override
    public Member findById(long id) {
        return memberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member",id));
    }


}
