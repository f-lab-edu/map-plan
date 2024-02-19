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
import static org.junit.jupiter.api.Assertions.*;

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
                .clockHolder(new TestClockProvider(Instant.now().toEpochMilli()))
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
    @DisplayName("saveRefreshToken 을 사용하여 RefreshToken 을 저장한다.")
    void saveRefreshToken() {
        //Given
        MemberCreate memberCreate = MemberCreate.builder()
                .name("testAOP")
                .email("testAOP@gmail.com")
                .phone("test1123")
                .password("test123")
                .build();
        Member member = memberService.saveMember(memberCreate);

        String refresh = jwtTokenizer
                .createRefreshToken(id, email, roles, new TestClockProvider(9999999999999999L));

        RefreshToken refreshToken1 = RefreshToken.from(member, refresh);

        //When

        RefreshToken refreshToken = refreshTokenService.saveRefreshToken(refreshToken1);
        //Then
        assertThat(refreshToken.getMember()).isEqualTo(member);
        assertThat(refreshToken.getToken()).isEqualTo(refresh);
    }

    @Test
    @DisplayName("findRefreshTokenTest 으로 테스트 할 수 있다.")
    void findRefreshToken() {

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


        //Given
        //When

        //Then
        assertThatThrownBy(() -> refreshTokenService.findRefreshToken("TEST11"))
                .isInstanceOf(ResourceNotFoundException.class);

    }


    @Test
    @DisplayName("회원 객체로 토큰을 찾아옵니다.")
    void findByMember() {
        //Given
        MemberCreate memberCreate = MemberCreate.builder()
                .name("test")
                .email("test@gmail.com")
                .phone("test1123")
                .password("test123")
                .build();
        Member member = memberService.saveMember(memberCreate);

        String refresh = jwtTokenizer
                .createRefreshToken(id, email, roles, new TestClockProvider(9999999999999999L));

        RefreshToken refreshToken1 = RefreshToken.from(member, refresh);
        RefreshToken refreshToken = refreshTokenService.saveRefreshToken(refreshToken1);
        //When
        Optional<RefreshToken> byMember = refreshTokenService.findByMember(member);

        //Then
        assertThat(byMember).isNotEmpty();
        assertThat(byMember.get()).isEqualTo(refreshToken);

    }

    @Test
    @DisplayName("updateRefreshToken 새로운 토큰으로 업데이트 합니다.")
    void updateRefreshToken() {
        //Given
        MemberCreate memberCreate = MemberCreate.builder()
                .name("test")
                .email("test@gmail.com")
                .phone("test1123")
                .password("test123")
                .build();
        Member member = memberService.saveMember(memberCreate);

        String refresh = jwtTokenizer
                .createRefreshToken(id, email, roles, new TestClockProvider(9999999999999999L));

        RefreshToken refreshToken = RefreshToken.from(member, refresh);
        RefreshToken saveRefreshToken = refreshTokenService.saveRefreshToken(refreshToken);



        String updateRefresh = jwtTokenizer
                .createRefreshToken(id, email, roles, new TestClockProvider(9999999888999999L));

        RefreshToken update = RefreshToken.from(member, updateRefresh);

        //When
        refreshTokenService.updateRefreshToken(update);
        RefreshToken updateTokenFind = refreshTokenService.findRefreshToken(updateRefresh);

        //Then
        assertThat(update.getToken()).isEqualTo(updateTokenFind.getToken());
    }


    @Test
    @DisplayName("로그아웃 시 토큰을 삭제할때 사용하는 메서드를 테스트 합니다.")
    void deleteToken() {
        //Given
        MemberCreate memberCreate = MemberCreate.builder()
                .name("test")
                .email("test@gmail.com")
                .phone("test1123")
                .password("test123")
                .build();
        Member member = memberService.saveMember(memberCreate);

        String refresh = jwtTokenizer
                .createRefreshToken(id, email, roles, new TestClockProvider(9999999999999999L));

        RefreshToken refreshToken = RefreshToken.from(member, refresh);
        refreshTokenService.saveRefreshToken(refreshToken);



        //When
        refreshTokenService.deleteToken(refresh);
        //Then
        assertThatThrownBy(()->refreshTokenService.findRefreshToken(refresh))
                .isInstanceOf(ResourceNotFoundException.class);
    }

}