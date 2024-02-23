package com.mapwithplan.mapplan.member.domain;


import com.mapwithplan.mapplan.common.exception.CertificationCodeNotMatchedException;
import com.mapwithplan.mapplan.common.timeutils.domain.BaseTime;
import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockProvider;
import com.mapwithplan.mapplan.common.uuidutils.service.port.UuidHolder;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * 회원 도메인 입니다.
 */
@Getter
public class Member extends BaseTime {

    private final Long id;

    private final String email;

    private final String password;

    private final String name;

    private final String phone;

    private final String statusMessage;

    private final String certificationCode;

    private final MemberStatus memberStatus;

    private final MemberRole memberRole;

    @Builder(toBuilder = true)
    public Member(LocalDateTime createdAt, LocalDateTime modifiedAt, Long id, String email, String password, String name, String phone, String statusMessage, String certificationCode, MemberStatus memberStatus, MemberRole memberRole) {
        super(createdAt, modifiedAt);
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.statusMessage = statusMessage;
        this.certificationCode = certificationCode;
        this.memberStatus = memberStatus;
        this.memberRole = memberRole;
    }



    public static Member from(MemberCreate memberCreate, TimeClockProvider clockHolder, UuidHolder uuidHolder, PasswordEncoder encoder){
        return Member.builder()
                .email(memberCreate.getEmail())
                .name(memberCreate.getName())
                .password(encoder.encode(memberCreate.getPassword()))
                .memberStatus(MemberStatus.PENDING)
                .certificationCode(uuidHolder.random())
                .phone(memberCreate.getPhone())
                .createdAt(clockHolder.clockProvider())
                .modifiedAt(clockHolder.clockProvider())
                .memberRole(MemberRole.MEMBER)
                .build();
    }

    /**
     * 현재 인증 코드와 들어온 인증 코드 파라미터의 일치 여부를 판단합니다.
     * @param certificationCode 인증 코드입니다.
     * @return
     */
    public Member certificate(String certificationCode) {
        if (!this.certificationCode.equals(certificationCode)) {
            throw new CertificationCodeNotMatchedException();
        }
        return Member.builder()
                .id(id)
                .email(email)
                .name(name)
                .password(password)
                .memberStatus(MemberStatus.ACTIVE)
                .phone(phone)
                .memberRole(memberRole)
                .certificationCode(certificationCode)
                .build();
    }

    /**
     * 회원 정보를 수정하는 도메인입니다.
     * @param editMember 번호와 상태메세지를 담고 있고 변경에 활용합니다.
     * @param clockProvider 수정 시간을 기록하기 위한 객체입니다.
     * @return
     */
    public Member edit(Member member,EditMember editMember,TimeClockProvider clockProvider){
        return member.toBuilder()
                .statusMessage(editMember.getStatusMessage())
                .phone(editMember.getPhone())
                .modifiedAt(clockProvider.clockProvider())
                .build();

    }
}
