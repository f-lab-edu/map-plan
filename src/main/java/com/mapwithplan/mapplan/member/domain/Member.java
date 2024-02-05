package com.mapwithplan.mapplan.member.domain;


import com.mapwithplan.mapplan.common.exception.CertificationCodeNotMatchedException;
import com.mapwithplan.mapplan.common.timeutils.domain.BaseTime;
import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
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

    private final EMemberStatus memberStatus;

    private final EMemberRole eMemberRole;
    @Builder
    public Member(LocalDateTime createdAt, LocalDateTime modifiedAt, Long id, String email, String password, String name, String phone, String statusMessage, String certificationCode, EMemberStatus memberStatus, EMemberRole eMemberRole) {
        super(createdAt, modifiedAt);
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.statusMessage = statusMessage;
        this.certificationCode = certificationCode;
        this.memberStatus = memberStatus;
        this.eMemberRole = eMemberRole;
    }



    public static Member from(MemberCreate memberCreate, TimeClockHolder clockHolder, UuidHolder uuidHolder, PasswordEncoder encoder){
        return Member.builder()
                .email(memberCreate.getEmail())
                .name(memberCreate.getName())
                .password(encoder.encode(memberCreate.getPassword()))
                .memberStatus(EMemberStatus.PENDING)
                .certificationCode(uuidHolder.random())
                .phone(memberCreate.getPhone())
                .createdAt(clockHolder.clockHold())
                .modifiedAt(clockHolder.clockHold())
                .eMemberRole(EMemberRole.MEMBER)
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
                .memberStatus(EMemberStatus.ACTIVE)
                .phone(phone)
                .eMemberRole(eMemberRole)
                .certificationCode(certificationCode)
                .build();
    }

    /**
     * 회원 정보를 수정하는 도메인입니다.
     * @param editMember 번호와 상태메세지를 담고 있고 변경에 활용합니다.
     * @param clockHolder 수정 시간을 기록하기 위한 객체입니다.
     * @return
     */
    public Member edit(EditMember editMember,TimeClockHolder clockHolder){
        return Member.builder()
                .id(id)
                .email(email)
                .name(name)
                .password(password)
                .memberStatus(EMemberStatus.ACTIVE)
                .phone(editMember.getPhone())
                .eMemberRole(eMemberRole)
                .certificationCode(certificationCode)
                .statusMessage(editMember.getStatusMessage())
                .createdAt(getCreatedAt())
                .modifiedAt(clockHolder.clockHold())
                .build();
    }
}
