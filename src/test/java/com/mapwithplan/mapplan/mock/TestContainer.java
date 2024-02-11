package com.mapwithplan.mapplan.mock;

import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockProvider;
import com.mapwithplan.mapplan.common.uuidutils.service.port.UuidHolder;
import com.mapwithplan.mapplan.jwt.util.JwtTokenizer;
import com.mapwithplan.mapplan.loginlogout.controller.AuthController;
import com.mapwithplan.mapplan.loginlogout.controller.port.LoginService;
import com.mapwithplan.mapplan.loginlogout.service.LoginServiceImpl;
import com.mapwithplan.mapplan.loginlogout.service.RefreshTokenService;
import com.mapwithplan.mapplan.loginlogout.service.port.RefreshTokenRepository;
import com.mapwithplan.mapplan.member.controller.MemberController;
import com.mapwithplan.mapplan.member.service.CertificationService;
import com.mapwithplan.mapplan.member.service.MemberServiceImpl;
import com.mapwithplan.mapplan.member.service.port.MailSender;
import com.mapwithplan.mapplan.member.service.port.MemberRepository;
import com.mapwithplan.mapplan.mock.planmock.FakePlanRepository;
import com.mapwithplan.mapplan.plan.controller.PlanController;
import com.mapwithplan.mapplan.plan.controller.port.PlanService;
import com.mapwithplan.mapplan.plan.service.PlanServiceImpl;
import com.mapwithplan.mapplan.plan.service.port.PlanRepository;
import lombok.Builder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestContainer {

    public final MailSender mailSender;

    public final MemberRepository memberRepository;

    public final MemberServiceImpl memberService;

    public final CertificationService certificationService;

    public final MemberController memberController;

    public final LoginService loginService;

    public final AuthController authController;

    public final RefreshTokenService refreshTokenService;
    public final RefreshTokenRepository refreshTokenRepository;

    public final JwtTokenizer jwtTokenizer;
    public final PlanRepository planRepository;
    public final PlanService planService;

    public final PlanController planController;

    @Builder
    public TestContainer(TimeClockProvider clockHolder, UuidHolder uuidHolder) {
        this.mailSender = new FakeMailSender();
        this.memberRepository = new FakeMemberRepository();
        this.certificationService = new CertificationService(this.mailSender);
        String accessSecret = "testtesttesttesttesttesttesttesttesttesttesttest";
        String refreshSecret = "testtesttesttesttesttesttesttesttesttesttesttest";

        this.jwtTokenizer = new JwtTokenizer(accessSecret,refreshSecret);

        MemberServiceImpl memberService = MemberServiceImpl.builder()
                .certificationService(this.certificationService)
                .clockHolder(clockHolder)
                .uuidHolder(uuidHolder)
                .jwtTokenizer(this.jwtTokenizer)
                .memberRepository(this.memberRepository)
                .passwordEncoder(new BCryptPasswordEncoder())
                .build();
        this.memberService = memberService;
        this.memberController = MemberController.builder()
                .memberService(memberService)
                .build();
        this.refreshTokenRepository = new FakeRefreshTokenRepository();
        this.refreshTokenService = RefreshTokenService.builder()
                .refreshTokenRepository(this.refreshTokenRepository)
                .build();
        this.loginService = LoginServiceImpl.builder()
                .jwtTokenizer(new JwtTokenizer(accessSecret, refreshSecret))
                .encoder(new FakePasswordEncoder())
                .timeClockProvider(clockHolder)
                .memberRepository(this.memberRepository)
                .refreshTokenService(this.refreshTokenService)
                .build();
        this.authController = AuthController.builder()
                .loginService(loginService)
                .build();



        //planContainer
        this.planRepository = new FakePlanRepository();
        this.planService = PlanServiceImpl.builder()
                .planRepository(this.planRepository)
                .clockHolder(clockHolder)
                .jwtTokenizer(this.jwtTokenizer)
                .memberRepository(this.memberRepository)
                .build();

        this.planController = PlanController.builder()
                .planService(this.planService)
                .build();
    }
}
