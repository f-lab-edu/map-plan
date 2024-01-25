package com.mapwithplan.mapplan.member.service;

import com.mapwithplan.mapplan.common.exception.CertificationCodeNotMatchedException;
import com.mapwithplan.mapplan.member.domain.EMemberStatus;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.domain.MemberCreate;
import com.mapwithplan.mapplan.mock.FakeMailSender;
import com.mapwithplan.mapplan.mock.FakeMemberRepository;
import com.mapwithplan.mapplan.mock.TestClockHolder;
import com.mapwithplan.mapplan.mock.TestUuidHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;


class MemberServiceImplTest {

    private MemberServiceImpl memberService;
    @BeforeEach
    void init(){
        FakeMemberRepository fakeMemberRepository = new FakeMemberRepository();
        FakeMailSender fakeMailSender = new FakeMailSender();
        this.memberService = MemberServiceImpl.builder()
                .memberRepository(fakeMemberRepository)
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                .clockHolder(new TestClockHolder(LocalDateTime.of(2024, 1, 24, 12, 30)))
                .certificationService(new CertificationService(fakeMailSender))
                .build();

        fakeMemberRepository.saveMember(Member.builder()
                .id(1L)
                .email("test@gmail.com")
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
    }

    @Test
    @DisplayName("MemberService 객체로 회원을 저장할 수 있다.")
    void MemberServiceSaveTest() {
        //Given
        MemberCreate memberCreate = MemberCreate.builder()
                .name("test")
                .email("test@gmail.com")
                .phone("test1123")
                .password("test123")
                .build();
        //When
        Member member = memberService.saveMember(memberCreate);
        //Then
        assertThat(member.getId()).isNotNull();
        assertThat(member.getPassword()).isEqualTo(memberCreate.getPassword());
        assertThat(member.getMemberStatus()).isEqualTo(EMemberStatus.PENDING);
        assertThat(member.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
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
}