package com.mapwithplan.mapplan.search.member.controller;

import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestClockProvider;
import com.mapwithplan.mapplan.mock.TestContainer;
import com.mapwithplan.mapplan.mock.TestUuidHolder;
import com.mapwithplan.mapplan.search.member.controller.response.FindMemberResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class SearchMemberControllerTest {

    TestContainer testContainer;
    @BeforeEach
    void init(){
        this.testContainer= new TestContainer(new TestClockProvider(1L), new TestUuidHolder("test"));

        for (long i = 1; i <= 4; i++) {
            Member member = Member.builder()
                    .email("test" + i + "@gmail.com")
                    .phone("123123")
                    .password("testTest")
                    .statusMessage("test" + i + " 입니다.")
                    .build();

            testContainer.memberRepository
                    .saveMember(member);

            Member member2 = Member.builder()
                    .email("testTest" + i + "@gmail.com")
                    .phone("123123")
                    .password("testTest")
                    .statusMessage("test" + i + " 입니다.")
                    .build();

            testContainer.memberRepository
                    .saveMember(member2);

        }

    }

    @Test
    @DisplayName("testController 에서 이메일로 회원을 검색합니다.")
    void SearchMemberControllerTest() {

        //Given
        String email = "test";
        String otherEmail = "testTest";
        //When
        ResponseEntity<List<FindMemberResponse>> test = testContainer
                .searchMemberController
                .searchMemberByEmail(email);
        ResponseEntity<List<FindMemberResponse>> other = testContainer
                .searchMemberController
                .searchMemberByEmail(otherEmail);
        //Then
        assertThat(test.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        assertThat(test.getBody().size()).isEqualTo(8);
        assertThat(other.getBody().size()).isEqualTo(4);
    }
}