package com.mapwithplan.mapplan.member.service;


import com.mapwithplan.mapplan.common.exception.DuplicateResourceException;
import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
import com.mapwithplan.mapplan.common.uuidutils.service.port.UuidHolder;
import com.mapwithplan.mapplan.jwt.util.JwtTokenizer;
import com.mapwithplan.mapplan.member.controller.port.MemberService;
import com.mapwithplan.mapplan.member.domain.EditMember;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.domain.MemberCreate;
import com.mapwithplan.mapplan.member.service.port.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
/**
 * MemberService Interface 를 상속 받아 구현한 클래스입니다.
 */
@Builder
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final CertificationService certificationService;

    private final TimeClockHolder clockHolder;

    private final UuidHolder uuidHolder;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenizer jwtTokenizer;


    /**
     * 회원 가입과 동시에 인증 메일을 발송하는 메서드입니다.
     * @param memberCreate 회원 생성을 위한 dto 입니다. email, password, name, phone 필드를 가지고 있습니다.
     * @return
     */
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

    /**
     * 토큰에 담겨있는 이메일을 통해 회원의 정보를 조회합니다.
     * @param accessToken 헤더에 있는 정보를 활용합니다.
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Member findByEmailUseAccessToken(String accessToken) {
        String email = getEmailFrom(accessToken);
        return memberRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Member", email));
    }

    /**
     * 큰에 담겨있는 이메일을 통해 회원의 정보를 조회합니다.
     * 회원의 상세 정보를 수정합니다. 수정된 시간과 정보를 수정합니다.
     * @param authorizationHeader 헤더에 있는 정보를 활용합니다.
     * @param editMember
     * @return
     */
    @Override
    @Transactional
    public Member editMemberDetail(String authorizationHeader, EditMember editMember) {
        String email = getEmailFrom(authorizationHeader);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Member", email));
        Member changeMember = member.edit(editMember, clockHolder);

        return  memberRepository.editMemberDetail(changeMember);
    }


    /**
     * 헤더 토큰을 활용해 이메일을 찾아내는 메서드 입니다.
     * @param authorizationHeader
     * @return
     */
    private String getEmailFrom(String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        Claims claims = jwtTokenizer.parseAccessToken(jwtToken);
        return claims.getSubject();
    }


}
