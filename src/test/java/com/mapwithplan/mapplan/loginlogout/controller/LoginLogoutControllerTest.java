package com.mapwithplan.mapplan.loginlogout.controller;

import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.loginlogout.domain.DeleteRefreshToken;
import com.mapwithplan.mapplan.loginlogout.domain.Login;
import com.mapwithplan.mapplan.loginlogout.domain.RefreshToken;
import com.mapwithplan.mapplan.member.domain.EMemberRole;
import com.mapwithplan.mapplan.member.domain.EMemberStatus;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestClockHolder;
import com.mapwithplan.mapplan.mock.TestContainer;
import com.mapwithplan.mapplan.mock.TestUuidHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class LoginLogoutControllerTest {

    @Test
    @DisplayName("로그인 정보를 보낼때  로그인에 성공한다.")
    void LoginLogoutControllerLoginTest() {
        //Given
        Member member = Member.builder()
                .email("testAOP@gmail.com")
                .name("testAOP")
                .memberStatus(EMemberStatus.ACTIVE)
                .password("123123")
                .phone("123123123")
                .eMemberRole(EMemberRole.MEMBER)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .modifiedAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .build();
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(9000000000000L))
                .uuidHolder(new TestUuidHolder("123123asd"))
                .build();
        testContainer.memberRepository.saveMember(member);

        Login login = new Login("testAOP@gmail.com", "123123");
        //When
        ResponseEntity<?> loginMember = testContainer.loginLogoutController.login(login);

        //Then
        assertThat(loginMember.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }

    @Test
    @DisplayName("잘못된 비밀 번호를 보낼 시 로그인에 실패한다.")
    void LoginLogoutControllerWrongPasswordFailTest() {
        //Given
        Member member = Member.builder()
                .email("testAOP@gmail.com")
                .name("testAOP")
                .memberStatus(EMemberStatus.ACTIVE)
                .password("123123")
                .phone("123123123")
                .eMemberRole(EMemberRole.MEMBER)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .modifiedAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .build();
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(9000000000000L))
                .uuidHolder(new TestUuidHolder("123123asd"))
                .build();
        testContainer.memberRepository.saveMember(member);

        Login login = new Login("testAOP@gmail.com", "1233");
        //When


        //Then
        assertThatThrownBy(()->testContainer.loginLogoutController.login(login))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("잘못된 메일을 보낼 시 로그인에 실패한다.")
    void LoginLogoutControllerWrongEmailFailTest() {
        //Given
        Member member = Member.builder()
                .email("testAOP@gmail.com")
                .name("testAOP")
                .memberStatus(EMemberStatus.ACTIVE)
                .password("123123")
                .phone("123123123")
                .eMemberRole(EMemberRole.MEMBER)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .modifiedAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .build();
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(9000000000000L))
                .uuidHolder(new TestUuidHolder("123123asd"))
                .build();
        testContainer.memberRepository.saveMember(member);

        Login login = new Login("testAOP@gail.com", "123123");
        //When


        //Then
        assertThatThrownBy(()->testContainer.loginLogoutController.login(login))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("PENDING 이면 로그인 못한다.")
    void LoginLogoutControllerPENDINGFailTest() {
        //Given
        Member member = Member.builder()
                .email("testAOP@gmail.com")
                .name("testAOP")
                .memberStatus(EMemberStatus.PENDING)
                .password("123123")
                .phone("123123123")
                .eMemberRole(EMemberRole.MEMBER)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .modifiedAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .build();
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(9000000000000L))
                .uuidHolder(new TestUuidHolder("123123asd"))
                .build();
        testContainer.memberRepository.saveMember(member);

        Login login = new Login("testAOP@gmail.com", "123123");
        //When


        //Then
        assertThatThrownBy(()->testContainer.loginLogoutController.login(login))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    @DisplayName("INACTIVE 이면 로그인 못한다.")
    void LoginLogoutControllerINACTIVEFailTest() {
        //Given
        Member member = Member.builder()
                .email("testAOP@gmail.com")
                .name("testAOP")
                .memberStatus(EMemberStatus.INACTIVE)
                .password("123123")
                .phone("123123123")
                .eMemberRole(EMemberRole.MEMBER)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .modifiedAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .build();
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(9000000000000L))
                .uuidHolder(new TestUuidHolder("123123asd"))
                .build();
        testContainer.memberRepository.saveMember(member);

        Login login = new Login("testAOP@gmail.com", "123123");
        //When


        //Then
        assertThatThrownBy(()->testContainer.loginLogoutController.login(login))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("로그아웃 시 리프레시 토큰을 삭제한다.")
    void LoginLogoutControllerLogoutDeleteRefreshTokenTest() {
        //Given

        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(9000000000000L))
                .uuidHolder(new TestUuidHolder("123123asd"))
                .build();
        String refresh = "TESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTEST";


        RefreshToken token = RefreshToken.builder()
                .token(refresh)
                .member(Member.builder().build())
                .build();
        testContainer
                .refreshTokenService
                .addRefreshToken(token);
        DeleteRefreshToken refreshToken = new DeleteRefreshToken(refresh);


        //When


        //Then
        ResponseEntity<DeleteRefreshToken> logout = testContainer.loginLogoutController.logout(refreshToken);
        assertThat(logout.getStatusCode())
                .isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(logout.getBody())
                .isEqualTo(refreshToken);
    }


}