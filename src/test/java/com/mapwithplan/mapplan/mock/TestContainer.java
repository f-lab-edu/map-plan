package com.mapwithplan.mapplan.mock;

import com.mapwithplan.mapplan.common.timeutils.infrastructure.ClockHolder;
import com.mapwithplan.mapplan.common.timeutils.service.port.LocalDateTimeClockHolder;
import com.mapwithplan.mapplan.common.uuidutils.service.port.UuidHolder;
import com.mapwithplan.mapplan.member.controller.MemberController;
import com.mapwithplan.mapplan.member.controller.MemberCreateController;
import com.mapwithplan.mapplan.member.service.CertificationService;
import com.mapwithplan.mapplan.member.service.MemberServiceImpl;
import com.mapwithplan.mapplan.member.service.port.MailSender;
import com.mapwithplan.mapplan.member.service.port.MemberRepository;
import lombok.Builder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestContainer {

    public final MailSender mailSender;

    public final MemberRepository memberRepository;

    public final MemberCreateController memberCreateController;

    public final CertificationService certificationService;

    public final MemberController memberController;
    @Builder
    public TestContainer(LocalDateTimeClockHolder clockHolder, UuidHolder uuidHolder) {
        this.mailSender = new FakeMailSender();
        this.memberRepository = new FakeMemberRepository();
        this.certificationService = new CertificationService(this.mailSender);
        MemberServiceImpl memberService = MemberServiceImpl.builder()
                .certificationService(this.certificationService)
                .clockHolder(clockHolder)
                .uuidHolder(uuidHolder)
                .memberRepository(this.memberRepository)
                .passwordEncoder(new BCryptPasswordEncoder())
                .build();
        this.memberCreateController = MemberCreateController
                .builder()
                .memberService(memberService)
                .build();
        this.memberController = MemberController.builder()
                .memberService(memberService)
                .build();
    }
}
