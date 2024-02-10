package com.mapwithplan.mapplan.member.controller;

import com.mapwithplan.mapplan.common.exception.CertificationCodeNotMatchedException;
import com.mapwithplan.mapplan.member.domain.EMemberStatus;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

class MemberControllerTest {

    private TestContainer testContainer;

    @BeforeEach
    void init(){
        testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(Instant.now().toEpochMilli()))
                .uuidHolder(() -> "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .build();

        testContainer.memberRepository.saveMember(Member.builder()
                .id(3L)
                .email("test3@naver.com")
                .password("test333")
                .phone("010-2222-2722")
                .name("테스트333")
                .eMemberRole(EMemberRole.MEMBER)
                .memberStatus(EMemberStatus.ACTIVE)
                .statusMessage("안녕하세요?")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .modifiedAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .build());
    }

    @Test
    @DisplayName("올바른 인증 이메일을 확인하고 검증합니다.")
    void MemberControllerTestVerifyEmail() {
        //Given
        Member member = Member.builder()
                .email("testAOP@gmail.com")
                .name("testAOP")
                .memberStatus(EMemberStatus.PENDING)
                .password("123123")
                .phone("123123123")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .modifiedAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .build();

        testContainer.memberRepository.saveMember(member);
        //When
        ResponseEntity<Void> voidResponseEntity = testContainer.memberController.verifyEmail(1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
        //Then
        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.memberRepository.findById(1).get().getMemberStatus()).isEqualTo(EMemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("잘못된 인증번호 이메일을 확인하고 검증합니다.")
    void MemberControllerTestNotVerifyEmail() {
        //Given
        Member member = Member.builder()
                .email("testAOP@gmail.com")
                .name("testAOP")
                .memberStatus(EMemberStatus.PENDING)
                .password("123123")
                .phone("010-1234-1234")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .modifiedAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .build();

        testContainer.memberRepository.saveMember(member);
        //When

        //Then
        assertThatThrownBy(() ->
                testContainer.memberController.verifyEmail(1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaq"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    @DisplayName("MemberCreateController 로 회원을 저장할 수 있다.")
    void MemberCreateControllerTestMemberCreate() {
        //Given
        MemberCreate memberCreate =
                new MemberCreate("testAOP", "testAOP", "testAOP", "testAOP");

        //When
        ResponseEntity<MemberCreateResponse> result = testContainer
                .memberController
                .memberCreate(memberCreate);
        //Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getClass()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("testAOP");
    }

    @Test
    @DisplayName("헤더에 있는 AccessToken 을 이용해 회원 정보를 가져온다.")
    void myPage() {
        //Given
        ArrayList<String> roles = new ArrayList<>();
        roles.add(EMemberRole.MEMBER.toString());
        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(3L, "test3@naver.com", roles, new TestClockHolder(Instant.now().toEpochMilli()));
        //When
        accessToken = "Bearer "+accessToken;
        ResponseEntity<MemberMyPageResponse> memberMyPageResponseResponseEntity = testContainer.memberController.myPage(accessToken);
        //Then

        assertThat(memberMyPageResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(memberMyPageResponseResponseEntity.getBody().getEmail()).isEqualTo("test3@naver.com");
        assertThat(memberMyPageResponseResponseEntity.getBody().getStatusMessage()).isEqualTo("안녕하세요?");

    }


    @Test
    @DisplayName("헤더에 있는 AccessToken 을 이용해 회원 자세한 정보를 가져온다.")
    void GetMyPageDetail() {
        //Given
        ArrayList<String> roles = new ArrayList<>();
        roles.add(EMemberRole.MEMBER.toString());
        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(3L, "test3@naver.com", roles, new TestClockHolder(Instant.now().toEpochMilli()));
        //When
        accessToken = "Bearer "+accessToken;
        ResponseEntity<MemberMyPageDetailResponse> memberMyPageDetailResponseResponseEntity = testContainer
                .memberController
                .myPageDetail(accessToken);
        //Then

        assertThat(memberMyPageDetailResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(memberMyPageDetailResponseResponseEntity.getBody().getEmail()).isEqualTo("test3@naver.com");
        assertThat(memberMyPageDetailResponseResponseEntity.getBody().getStatusMessage()).isEqualTo("안녕하세요?");
        assertThat(memberMyPageDetailResponseResponseEntity.getBody().getPhone()).isEqualTo("010-2222-2722");

    }


    @Test
    @DisplayName("헤더에 있는 AccessToken 을 이용해 회원 자세한 정보를 가져온 후 수정한다.")
    void EditMyPageDetail() {
        //Given
        ArrayList<String> roles = new ArrayList<>();
        roles.add(EMemberRole.MEMBER.toString());
        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(3L, "test3@naver.com", roles, new TestClockHolder(Instant.now().toEpochMilli()));
        //When
        accessToken = "Bearer "+accessToken;

        EditMember editMember = new EditMember("안녕", "010-6666-2222");
        ResponseEntity<MemberMyPageDetailResponse> memberMyPageDetailResponseResponseEntity = testContainer
                .memberController
                .myPageDetailEdit(accessToken, editMember);
        //Then

        assertThat(memberMyPageDetailResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(memberMyPageDetailResponseResponseEntity.getBody().getStatusMessage()).isNotNull();
        assertThat(memberMyPageDetailResponseResponseEntity.getBody().getStatusMessage()).isEqualTo(editMember.getStatusMessage());
        assertThat(memberMyPageDetailResponseResponseEntity.getBody().getPhone()).isEqualTo(editMember.getPhone());

    }

}