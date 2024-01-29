package com.mapwithplan.mapplan.member.service;


import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.common.timeutils.service.port.LocalDateTimeClockHolder;
import com.mapwithplan.mapplan.common.uuidutils.service.port.UuidHolder;
import com.mapwithplan.mapplan.member.controller.port.MemberService;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.domain.MemberCreate;
import com.mapwithplan.mapplan.member.service.port.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * MemberService Interface 를 상속 받아 구현한 클래스입니다.
 */
@Builder
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final CertificationService certificationService;

    private final LocalDateTimeClockHolder clockHolder;

    private final UuidHolder uuidHolder;

    /**
     * 회원 가입과 동시에 인증 메일을 발송하는 메서드입니다.
     * @param memberCreate 회원 생성을 위한 dto 입니다. email, password, name, phone 필드를 가지고 있습니다.
     * @return
     */
    @Transactional
    @Override
    public Member saveMember(MemberCreate memberCreate) {
        Member member = Member.from(memberCreate, clockHolder,uuidHolder);
        member = memberRepository.saveMember(member);
        certificationService.send(memberCreate.getEmail(),member.getId(),member.getCertificationCode());
        return member;
    }

    /**
     * 인증메일을 확인하는 메서드입니다. 인증 코드와 일치하는지 확인합니다.
     * @param id  controller 에서 @GetMapping("/{id}/verify") 를 위한 id 입니다.
     * @param certificationCode 인증 코드는 공통 유틸인 UuidHolder 를 통해 생성된 코드입니다.
     */
    @Transactional
    @Override
    public void verifyEmail(long id, String certificationCode) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member", id));
        member = member.certificate(certificationCode);
        memberRepository.saveMember(member);
    }

    /**
     * 아이디를 찾는 메서드 입니다.
     * @param id 회원 Id 입니다.
     * @return
     */
    @Override
    public Member findById(long id) {
        return memberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member",id));
    }


}
