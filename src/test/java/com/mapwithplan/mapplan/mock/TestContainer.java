package com.mapwithplan.mapplan.mock;

import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
import com.mapwithplan.mapplan.common.uuidutils.service.port.UuidHolder;
import com.mapwithplan.mapplan.jwt.util.JwtTokenizer;
import com.mapwithplan.mapplan.loginlogout.controller.LoginLogoutController;
import com.mapwithplan.mapplan.loginlogout.controller.port.LoginService;
import com.mapwithplan.mapplan.loginlogout.service.LoginServiceImpl;
import com.mapwithplan.mapplan.loginlogout.service.RefreshTokenService;
import com.mapwithplan.mapplan.loginlogout.service.port.RefreshTokenRepository;
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

    public final LoginService loginService;

    public final LoginLogoutController loginLogoutController;

    public final RefreshTokenService refreshTokenService;
    public final RefreshTokenRepository refreshTokenRepository;
    @Builder
    public TestContainer(TimeClockHolder clockHolder, UuidHolder uuidHolder) {
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
        String accessSecret = "testtesttesttesttesttesttesttesttesttesttesttest";
        String refreshSecret = "testtesttesttesttesttesttesttesttesttesttesttest";
        this.refreshTokenRepository = new FakeRefreshTokenRepository();
        this.loginService = LoginServiceImpl.builder()
                .refreshTokenRepository(this.refreshTokenRepository)
                .jwtTokenizer(new JwtTokenizer(accessSecret, refreshSecret))
                .encoder(new FakePasswordEncoder())
                .timeClockHolder(clockHolder)
                .memberRepository(this.memberRepository)
                .build();
        this.loginLogoutController = LoginLogoutController.builder()
                .loginService(loginService)
                .build();
        this.refreshTokenService = RefreshTokenService.builder()
                .refreshTokenRepository(this.refreshTokenRepository)
                .build();
    }
}
