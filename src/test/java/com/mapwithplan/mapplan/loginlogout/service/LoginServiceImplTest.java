package com.mapwithplan.mapplan.loginlogout.service;

import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.jwt.util.JwtTokenizer;
import com.mapwithplan.mapplan.loginlogout.controller.response.LoginResponse;
import com.mapwithplan.mapplan.loginlogout.domain.Login;
import com.mapwithplan.mapplan.loginlogout.domain.RefreshToken;
import com.mapwithplan.mapplan.member.domain.EMemberRole;
import com.mapwithplan.mapplan.member.domain.EMemberStatus;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.service.CertificationService;
import com.mapwithplan.mapplan.member.service.MemberServiceImpl;
import com.mapwithplan.mapplan.mock.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class LoginServiceImplTest {

    private LoginServiceImpl loginService;
    private MemberServiceImpl memberService;
    private FakeRefreshTokenRepository fakeRefreshTokenRepository;
    @BeforeEach
    void init(){
        FakeMemberRepository fakeMemberRepository = new FakeMemberRepository();
        FakeMailSender fakeMailSender = new FakeMailSender();
        this.fakeRefreshTokenRepository = new FakeRefreshTokenRepository();
        this.memberService = MemberServiceImpl.builder()
                .memberRepository(fakeMemberRepository)
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                .clockHolder(new TestClockProvider(Instant.now().toEpochMilli()))
                .certificationService(new CertificationService(fakeMailSender))
                .passwordEncoder(new BCryptPasswordEncoder())
                .build();

        Member member = Member.builder()
                .id(1L)
                .email("testAOP@gmail.com")
                .password("test123")
                .phone("010-1234-1234")
                .name("테스트")
                .memberStatus(EMemberStatus.INACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .createdAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .modifiedAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .build();
        Member member2 = Member.builder()
                .id(2L)
                .email("test333@gmail.com")
                .password("test333")
                .phone("010-2222-2222")
                .name("테스트333")
                .eMemberRole(EMemberRole.MEMBER)
                .memberStatus(EMemberStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .modifiedAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .build();
        Member member3 = Member.builder()
                .id(3L)
                .email("test4444@gmail.com")
                .password("test333")
                .phone("010-2222-2222")
                .name("테스트333")
                .eMemberRole(EMemberRole.MEMBER)
                .memberStatus(EMemberStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .modifiedAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .build();
        fakeRefreshTokenRepository.save(
                RefreshToken.builder()
                        .member(member3)
                        .token("testAOP")
                        .id(1L).build()
        );
        fakeMemberRepository.saveMember(member2);
        fakeMemberRepository.saveMember(member);
        fakeMemberRepository.saveMember(member3);

        this.loginService = LoginServiceImpl.builder()
                .encoder(new FakePasswordEncoder())
                .memberRepository(fakeMemberRepository)
                .jwtTokenizer(new JwtTokenizer("testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest",
                        "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest"))
                .timeClockProvider(new TestClockProvider(123L))
                .refreshTokenRepository(fakeRefreshTokenRepository)
                .build();
    }
    @Test
    @DisplayName("로그인시 휴면 계정은 막는 테스트 합니다.")
    void loginTest() {
        //Given
        Login login = new Login("testAOP@gmail.com", "test123");
        //When
        //Then
        assertThatThrownBy(()->loginService.login(login))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("첫 로그인시 토큰을 생성합니다.")
    void firstLoginMakeTokenTest() {
        //Given
        Login login = new Login("test333@gmail.com", "test333");
        //When
        LoginResponse loginResponse = loginService.login(login);
        //Then


        RefreshToken byToken = fakeRefreshTokenRepository.findByToken(loginResponse.getRefreshToken());
        assertThat(loginResponse.getEmail()).isEqualTo(login.getEmail());
        assertThat(loginResponse.getRefreshToken()).isEqualTo(byToken.getToken());
        assertThat(loginResponse.getAccessToken()).isNotEmpty();
        assertThat(loginResponse.getMemberRole()).isEqualTo(EMemberRole.MEMBER);
    }

    @Test
    @DisplayName("다시 로그인시 토큰을 생성합니다.")
    void ReLoginMakeTokenTest() {
        //Given

        Login login = new Login("test4444@gmail.com", "test333");
        //When
        LoginResponse loginResponse = loginService.login(login);
        //Then

        RefreshToken byToken = fakeRefreshTokenRepository.findByToken(loginResponse.getRefreshToken());
        assertThat(loginResponse.getEmail()).isEqualTo(login.getEmail());
        assertThat(loginResponse.getRefreshToken()).isEqualTo(byToken.getToken());

    }
    
    @Test
    @DisplayName("로그아웃 시 리프레시 토큰을 삭제합니다.")
    void logoutTest() {
        //Given
        String refreshToken = "testAOP";
        //When
        loginService.logout(refreshToken);
        //Then
        assertThatThrownBy(()->fakeRefreshTokenRepository.findByToken(refreshToken))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}