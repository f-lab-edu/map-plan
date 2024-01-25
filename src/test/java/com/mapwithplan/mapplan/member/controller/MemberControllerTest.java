package com.mapwithplan.mapplan.member.controller;

import com.mapwithplan.mapplan.common.exception.CertificationCodeNotMatchedException;
import com.mapwithplan.mapplan.member.domain.EMemberStatus;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.domain.MemberCreate;
import com.mapwithplan.mapplan.mock.TestClockHolder;
import com.mapwithplan.mapplan.mock.TestContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberControllerTest {
    @Test
    @DisplayName("올바른 인증 이메일을 확인하고 검증합니다.")
    void MemberControllerTestVerifyEmail() {
        //Given
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("test")
                .memberStatus(EMemberStatus.PENDING)
                .password("123123")
                .phone("123123123")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .modifiedAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .build();
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.memberRepository.saveMember(member);
        //When
        ResponseEntity<Void> voidResponseEntity = testContainer.memberController.verifyEmail(1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
        //Then
        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.memberRepository.findById(1).get().getMemberStatus()).isEqualTo(EMemberStatus.ACTIVE);
//        assertThat(voidResponseEntity.)
    }

    @Test
    @DisplayName("잘못된 인증번호 이메일을 확인하고 검증합니다.")
    void MemberControllerTestNotVerifyEmail() {
        //Given
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("test")
                .memberStatus(EMemberStatus.PENDING)
                .password("123123")
                .phone("123123123")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .modifiedAt(LocalDateTime.of(2024, 12, 13, 12, 13))
                .build();
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.memberRepository.saveMember(member);
        //When

        //Then
        assertThatThrownBy(() ->
                testContainer.memberController.verifyEmail(1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaq"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}