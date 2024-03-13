package com.mapwithplan.mapplan.search.member.service;

import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestClockProvider;
import com.mapwithplan.mapplan.mock.TestContainer;
import com.mapwithplan.mapplan.mock.TestUuidHolder;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;



class SearchMemberServiceImplTest {

    TestContainer testContainer;
    @BeforeEach
    void init(){
        this.testContainer= new TestContainer(new TestClockProvider(1L), new TestUuidHolder("test"));

        for (long i = 1; i <= 4; i++) {
            Member member = Member.builder()
                    .id(i)
                    .email("test" + i + "@gmail.com")
                    .phone("123123")
                    .password("testTest")
                    .statusMessage("test" + i + " 입니다.")
                    .build();

            testContainer.memberRepository
                    .saveMember(member);

            Member member2 = Member.builder()
                    .id(i+5)
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
    @DisplayName("이메일로 회원을 검색합니다.")
    void searchMemberByEmail(){
        //given
        String email = "test";
        String otherEmail = "testTest";

        //when


        List<Member> memberByEmail = testContainer.searchMemberService.findMemberByEmail(email);
        List<Member> byMemberEmailOtherCase = testContainer.searchMemberService.findMemberByEmail(otherEmail);
        //then
        Assertions.assertThat(memberByEmail.size()).isEqualTo(8);
        Assertions.assertThat(byMemberEmailOtherCase.size()).isEqualTo(4);
    }




}