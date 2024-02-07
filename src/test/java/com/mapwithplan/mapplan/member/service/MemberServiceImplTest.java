package com.mapwithplan.mapplan.member.service;

import com.mapwithplan.mapplan.common.exception.CertificationCodeNotMatchedException;
import com.mapwithplan.mapplan.common.exception.DuplicateResourceException;
import com.mapwithplan.mapplan.jwt.util.JwtTokenizer;
import com.mapwithplan.mapplan.member.domain.*;
import com.mapwithplan.mapplan.mock.*;
import com.mapwithplan.mapplan.mock.membermock.FakeMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;


class MemberServiceImplTest {

    private MemberServiceImpl memberService;
    private JwtTokenizer jwtTokenizer;
    @BeforeEach
    void init(){
        FakeMemberRepository fakeMemberRepository = new FakeMemberRepository();
        FakeMailSender fakeMailSender = new FakeMailSender();
        this.jwtTokenizer = new JwtTokenizer("testsettsetsetsetsetestestsettsetsetsetsetestestsettsetsetsetsetes"
                ,"lasdkfgaslkdjfhasldkjfsadlfknlkdajvnbsfdlkblasdkfgaslkdjfhasldkjfsadlfknlkdajvnbsfdlkb");
        this.memberService = MemberServiceImpl.builder()
                .memberRepository(fakeMemberRepository)
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                .clockHolder(new TestClockHolder(Instant.now().toEpochMilli()))
                .certificationService(new CertificationService(fakeMailSender))
                .passwordEncoder(new BCryptPasswordEncoder())
                .jwtTokenizer(this.jwtTokenizer)
                .build();

        fakeMemberRepository.saveMember(Member.builder()
                .id(1L)
                .email("testAOP@gmail.com")
                .password("test123")
                .phone("010-1234-1234")
                .name("테스트")
                .memberStatus(EMemberStatus.PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .createdAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .modifiedAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .build());

        fakeMemberRepository.saveMember(Member.builder()
                .id(2L)
                .email("test333@gmail.com")
                .password("test333")
                .phone("010-2222-2222")
                .name("테스트333")
                .memberStatus(EMemberStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .modifiedAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .build());
        fakeMemberRepository.saveMember(Member.builder()
                .id(3L)
                .email("test3@naver.com")
                .password("test333")
                .phone("010-2222-2722")
                .name("테스트333")
                        .eMemberRole(EMemberRole.MEMBER)
                .memberStatus(EMemberStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .modifiedAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .build());
    }

    @Test
    @DisplayName("MemberService 객체로 회원을 저장할 수 있다.")
    void MemberServiceSaveTest() {
        //Given
        MemberCreate memberCreate = MemberCreate.builder()
                .name("testAOP")
                .email("test12@gmail.com")
                .phone("test1123")
                .password("test123")
                .build();
        //When
        Member member = memberService.saveMember(memberCreate);
        //Then
        assertThat(member.getId()).isNotNull();
        assertThat(member.getPassword()).isNotEqualTo(memberCreate.getPassword());
        assertThat(member.getMemberStatus()).isEqualTo(EMemberStatus.PENDING);
        assertThat(member.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
    }

    @Test
    @DisplayName("MemberService 객체로 중복 회원은 저장할 수 없다.")
    void MemberServiceDontSaveDuplicateMemberTest() {
        //Given
        MemberCreate memberCreate = MemberCreate.builder()
                .name("testAOP")
                .email("testAOP@gmail.com")
                .phone("test1123")
                .password("test123")
                .build();
        //When
        //Then
        Assertions.assertThatThrownBy(()->memberService.saveMember(memberCreate))
                .isInstanceOf(DuplicateResourceException.class);

    }
    @Test
    @DisplayName("회원 서비스로 이메일을 검증하면 회원의 상태가 ACTIVE 가 된다.")
    void MemberServiceImplTestVerifyEmail() {
        //Given
        //When
        memberService.verifyEmail(2,"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

        //Then
        Member byId = memberService.findById(2);
        assertThat(byId.getMemberStatus()).isEqualTo(EMemberStatus.ACTIVE);

    }

    @Test
    @DisplayName("회원 서비스로 잘못된 이메일을 검증하면 예외가 발생한다.")
    void MemberServiceImplTestNotVerifyEmail() {
        //Given

        //When

        //Then
        assertThatThrownBy(()-> memberService.verifyEmail(2,"123123"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);

    }


    @Test
    @DisplayName("헤더에 있는 AccessToken 에서 이메일을 사용해 회원을 찾아온다.")
    void findEmailByAccessToken() {
        //Given

        Member byId = memberService.findById(3L);
        ArrayList<String> roles = new ArrayList<>();
        roles.add(byId.getEMemberRole().toString());
        String accessToken = jwtTokenizer.createAccessToken(byId.getId(), byId.getEmail(), roles, new TestClockHolder(900000000000000L));
        //When
        accessToken = "Bearer "+accessToken;

        Member emailUseAccessToken = memberService
                .findByEmailUseAccessToken(accessToken);

        //then
        assertThat(emailUseAccessToken).isEqualTo(byId);

    }

    @Test
    @DisplayName("MemberService 를 이용해 회원 정보를 수정할 수 있다.")
    void editMemberServiceTest() {
        //Given
        Member byId = memberService.findById(3L);
        ArrayList<String> roles = new ArrayList<>();
        roles.add(byId.getEMemberRole().toString());
        String accessToken = jwtTokenizer.createAccessToken(byId.getId(), byId.getEmail(), roles, new TestClockHolder(900000000000000L));
        EditMember editMember = new EditMember("1234", "010-2313-1234");
        //When
        accessToken = "Bearer "+accessToken;
        Member member = memberService.editMemberDetail(accessToken, editMember);

        //Then
        assertThat(member.getStatusMessage()).isEqualTo(editMember.getStatusMessage());
        assertThat(member.getPhone()).isEqualTo(editMember.getPhone());
    }
}