package com.mapwithplan.mapplan.member.domain;

import com.mapwithplan.mapplan.common.exception.CertificationCodeNotMatchedException;
import com.mapwithplan.mapplan.mock.TestClockHolder;
import com.mapwithplan.mapplan.mock.TestUuidHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.*;

class MemberTest {

    @Test
    @DisplayName("MemberCreate 객체로 Member 객체를 생성할 수 있다")
    void MemberCreateTest() {
        //Given
        MemberCreate memberCreate = MemberCreate.builder()
                .email("test@gmail.com")
                .password("test123")
                .phone("010-1234-1234")
                .name("테스트")
                .build();
        long epochMilli = Instant.parse("2022-01-01T12:00:00Z").toEpochMilli();
        //When
        Member from = Member.from(memberCreate ,
                new TestClockHolder(epochMilli),
                new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                new BCryptPasswordEncoder());
        //Then
        assertThat(from.getId()).isNull();
        assertThat(from.getEmail()).isEqualTo(memberCreate.getEmail());
        assertThat(from.getPassword()).isNotEqualTo(memberCreate.getPassword());
        assertThat(from.getPhone()).isEqualTo(memberCreate.getPhone());
        assertThat(from.getName()).isEqualTo(memberCreate.getName());
        assertThat(from.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        assertThat(from.getMemberStatus()).isEqualTo(EMemberStatus.PENDING);
        assertThat(from.getCreatedAt()).isEqualTo(LocalDateTime.ofInstant(Instant.parse("2022-01-01T12:00:00Z"), ZoneId.systemDefault()));


    }

    @Test
    @DisplayName("회원이 유효한 인증 코드로 계정을 활성화한다.")
    void MemberCertificationTest() {
        //Given
        Member member = Member.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("test123")
                .phone("010-1234-1234")
                .name("테스트")
                .memberStatus(EMemberStatus.PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        //When
        Member certificate = member.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        //Then
        assertThat(certificate.getMemberStatus()).isEqualTo(EMemberStatus.ACTIVE);

    }

    @Test
    @DisplayName("회원이 잘못된 인증 코드로 계정을 요청하면 예외가 발생한다.")
    void MemberNotCertification() {
        //Given
        Member member = Member.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("test123")
                .phone("010-1234-1234")
                .name("테스트")
                .memberStatus(EMemberStatus.PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        //When

        //Then
        assertThatThrownBy(()-> member.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaa123aaaaaaa"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}