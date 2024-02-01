package com.mapwithplan.mapplan.member.domain;


import com.mapwithplan.mapplan.common.exception.CertificationCodeNotMatchedException;
import com.mapwithplan.mapplan.common.timeutils.domain.BaseTime;
import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
import com.mapwithplan.mapplan.common.uuidutils.service.port.UuidHolder;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

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
}
