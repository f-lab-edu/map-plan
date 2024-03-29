package com.mapwithplan.mapplan.member.infrastructure.entity;


import com.mapwithplan.mapplan.member.domain.MemberStatus;
import com.mapwithplan.mapplan.member.domain.MemberRole;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.common.timeutils.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


//todo 프로필 이미지 파일 넣기 기능 추가
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "phone",length = 14)
    private String phone;

    @Column(name = "status_message")
    private String statusMessage;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Column(name = "certification_code")
    private String certificationCode;

    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Builder
    public MemberEntity(LocalDateTime createdAt, LocalDateTime modifiedAt, Long id, String email, String password, String name, String phone, String statusMessage, MemberStatus memberStatus, String certificationCode, MemberRole memberRole) {
        super(createdAt, modifiedAt);
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.statusMessage = statusMessage;
        this.memberStatus = memberStatus;
        this.certificationCode = certificationCode;
        this.memberRole = memberRole;
    }

    public static MemberEntity from(Member member){
        return MemberEntity.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .name(member.getName())
                .phone(member.getPhone())
                .memberStatus(member.getMemberStatus())
                .statusMessage(member.getStatusMessage())
                .certificationCode(member.getCertificationCode())
                .modifiedAt(member.getModifiedAt())
                .createdAt(member.getCreatedAt())
                .memberRole(member.getMemberRole())
                .build();

    }

    public Member toModel(){
        return Member.builder()
                .id(id)
                .phone(phone)
                .name(name)
                .email(email)
                .memberStatus(memberStatus)
                .certificationCode(certificationCode)
                .password(password)
                .statusMessage(statusMessage)
                .memberRole(memberRole)
                .build();
    }

    public void editFrom(Member editMember){
        this.statusMessage = editMember.getStatusMessage();
        this.phone = editMember.getPhone();
        changeModifiedAt(editMember.getModifiedAt());
    }


}
