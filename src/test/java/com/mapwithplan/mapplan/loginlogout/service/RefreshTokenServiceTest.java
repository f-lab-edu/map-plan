package com.mapwithplan.mapplan.loginlogout.service;

import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.jwt.util.JwtTokenizer;
import com.mapwithplan.mapplan.loginlogout.domain.RefreshToken;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.domain.MemberCreate;
import com.mapwithplan.mapplan.member.service.CertificationService;
import com.mapwithplan.mapplan.member.service.MemberServiceImpl;
import com.mapwithplan.mapplan.mock.*;
import com.mapwithplan.mapplan.mock.membermock.FakeMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RefreshTokenServiceTest {

    private RefreshTokenService refreshTokenService;
    private JwtTokenizer jwtTokenizer;
    private MemberServiceImpl memberService;

    String refreshSecret = "abcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefgabcdefg";
    String accessSecret= "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest";
    String email = "testAOP@gmail.com";
    List<String> roles = List.of("MEMBER");
    Long id = 1L;
    final Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L;
    @BeforeEach
    void init(){
        FakeMemberRepository fakeMemberRepository = new FakeMemberRepository();
        FakeMailSender fakeMailSender = new FakeMailSender();
        this.memberService = MemberServiceImpl.builder()
                .memberRepository(fakeMemberRepository)
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                .clockHolder(new TestClockHolder(Instant.now().toEpochMilli()))
                .certificationService(new CertificationService(fakeMailSender))
                .passwordEncoder(new BCryptPasswordEncoder())
                .build();
        FakeRefreshTokenRepository fakeRefreshTokenRepository = new FakeRefreshTokenRepository();
        fakeRefreshTokenRepository.save(RefreshToken.builder()
                .token("TEST")
                .id(1L)
                .member(Member.builder().name("testAOP").build())
                .build());
        this.refreshTokenService =new RefreshTokenService(fakeRefreshTokenRepository);

        this.jwtTokenizer = new JwtTokenizer(accessSecret,refreshSecret);
    }

    @Test
    @DisplayName("addRefreshToken 을 사용하여 RefreshToken 을 저장한다.")
    void addRefreshTokenTest() {
        //Given
        MemberCreate memberCreate = MemberCreate.builder()
                .name("testAOP")
                .email("testAOP@gmail.com")
                .phone("test1123")
                .password("test123")
                .build();
        Member member = memberService.saveMember(memberCreate);

        String refresh = jwtTokenizer
                .createRefreshToken(id, email, roles, new TestClockHolder(9999999999999999L));

        RefreshToken refreshToken1 = RefreshToken.from(member, refresh);

        //When

        RefreshToken refreshToken = refreshTokenService.addRefreshToken(refreshToken1);
        //Then
        assertThat(refreshToken.getMember()).isEqualTo(member);
        assertThat(refreshToken.getToken()).isEqualTo(refresh);
    }

    @Test
    @DisplayName("findRefreshTokenTest 으로 테스트 할 수 있다.")
    void findRefreshTokenTest() {

        String refreshToken = "TEST";
        //Given
        //When
        RefreshToken test = refreshTokenService.findRefreshToken("TEST");

        //Then
        assertThat(test.getToken()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("findRefreshTokenTest 으로 없을시 예외가 발생한다. ")
    void findRefreshTokenExceptionTest() {

        String refreshToken = "TEST";
        //Given
        //When

        //Then
        assertThatThrownBy(() -> refreshTokenService.findRefreshToken("TEST11"))
                .isInstanceOf(ResourceNotFoundException.class);

    }

}