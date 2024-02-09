package com.mapwithplan.mapplan.mock;

import com.mapwithplan.mapplan.PlanShareFriendship.controller.PlanShareFriendshipController;
import com.mapwithplan.mapplan.PlanShareFriendship.controller.port.PlanShareFriendshipService;
import com.mapwithplan.mapplan.PlanShareFriendship.service.PlanShareFriendshipServiceImpl;
import com.mapwithplan.mapplan.PlanShareFriendship.service.port.PlanShareFriendshipRepository;
import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
import com.mapwithplan.mapplan.common.uuidutils.service.port.UuidHolder;
import com.mapwithplan.mapplan.friendship.controller.FriendshipController;
import com.mapwithplan.mapplan.friendship.controller.port.FriendshipService;
import com.mapwithplan.mapplan.friendship.service.FriendshipServiceImpl;
import com.mapwithplan.mapplan.friendship.service.port.FriendshipRepository;
import com.mapwithplan.mapplan.jwt.util.JwtTokenizer;
import com.mapwithplan.mapplan.loginlogout.controller.LoginLogoutController;
import com.mapwithplan.mapplan.loginlogout.controller.port.LoginService;
import com.mapwithplan.mapplan.loginlogout.service.LoginServiceImpl;
import com.mapwithplan.mapplan.loginlogout.service.RefreshTokenService;
import com.mapwithplan.mapplan.loginlogout.service.port.RefreshTokenRepository;
import com.mapwithplan.mapplan.member.controller.MemberController;
import com.mapwithplan.mapplan.member.service.CertificationService;
import com.mapwithplan.mapplan.member.service.MemberServiceImpl;
import com.mapwithplan.mapplan.member.service.port.MailSender;
import com.mapwithplan.mapplan.member.service.port.MemberRepository;
import com.mapwithplan.mapplan.mock.friendshipmock.FakeFriendshipRepository;
import com.mapwithplan.mapplan.mock.membermock.FakeMemberRepository;
import com.mapwithplan.mapplan.mock.planmock.FakePlanRepository;
import com.mapwithplan.mapplan.mock.plansharefriendshipmock.FakePlanShareFriendshipRepository;
import com.mapwithplan.mapplan.plan.controller.PlanController;
import com.mapwithplan.mapplan.plan.controller.port.PlanService;
import com.mapwithplan.mapplan.plan.service.PlanServiceImpl;
import com.mapwithplan.mapplan.plan.service.port.PlanRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class TestContainer {

    public final MailSender mailSender;

    public final MemberRepository memberRepository;

    public final MemberServiceImpl memberService;

    public final CertificationService certificationService;

    public final MemberController memberController;

    public final LoginService loginService;

    public final LoginLogoutController loginLogoutController;

    public final RefreshTokenService refreshTokenService;
    public final RefreshTokenRepository refreshTokenRepository;

    public final JwtTokenizer jwtTokenizer;


    // plan
    public final PlanRepository planRepository;
    public final PlanService planService;
    public final PlanController planController;


    //friendship
    public final FriendshipRepository friendshipRepository;
    public final FriendshipService friendshipService;

    public final FriendshipController friendshipController;




    // PlanShareFriendship
    public final PlanShareFriendshipRepository planShareFriendshipRepository;
    public final PlanShareFriendshipService planShareFriendshipService;
    public final PlanShareFriendshipController planShareFriendshipController;



    @Builder
    public TestContainer(TimeClockHolder clockHolder, UuidHolder uuidHolder) {
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


        //friendship
        this.friendshipRepository = new FakeFriendshipRepository();

        this.friendshipService = FriendshipServiceImpl.builder()
                .clockHolder(clockHolder)
                .jwtTokenizer(jwtTokenizer)
                .friendshipRepository(friendshipRepository)
                .memberRepository(memberRepository)
                .build();

        this.friendshipController = FriendshipController.builder()
                .friendshipService(this.friendshipService)
                .build();


        // PlanShareFriendship

        this.planShareFriendshipRepository = new FakePlanShareFriendshipRepository();
        this.planShareFriendshipService = PlanShareFriendshipServiceImpl.builder()
                .planShareFriendshipRepository(this.planShareFriendshipRepository)
                .friendshipRepository(this.friendshipRepository)
                .planRepository(this.planRepository)
                .timeClockHolder(clockHolder)
                .build();
        this.planShareFriendshipController= PlanShareFriendshipController.builder()
                .planShareFriendshipService(this.planShareFriendshipService)
                .build();
    }
}
