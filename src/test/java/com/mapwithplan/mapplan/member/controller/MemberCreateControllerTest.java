package com.mapwithplan.mapplan.member.controller;

import com.mapwithplan.mapplan.member.controller.response.MemberCreateResponse;
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


class MemberCreateControllerTest {

    @Test
    @DisplayName("MemberCreateController 로 회원을 저장할 수 있다.")
    void MemberCreateControllerTestMemberCreate() {
        //Given
        MemberCreate memberCreate =
                new MemberCreate("test", "test", "test", "test");
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(LocalDateTime.of(2024,1,13,12,30)))
                .uuidHolder(() -> "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .build();
        //When
        ResponseEntity<MemberCreateResponse> result = testContainer
                .memberCreateController
                .memberCreate(memberCreate);
        //Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getClass()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("test");
    }

}