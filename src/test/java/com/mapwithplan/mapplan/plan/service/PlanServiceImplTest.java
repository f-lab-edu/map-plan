package com.mapwithplan.mapplan.plan.service;

import com.mapwithplan.mapplan.common.exception.UnauthorizedServiceException;
import com.mapwithplan.mapplan.member.domain.MemberRole;
import com.mapwithplan.mapplan.member.domain.MemberStatus;
import com.mapwithplan.mapplan.member.domain.Member;

import com.mapwithplan.mapplan.mock.TestClockProvider;
import com.mapwithplan.mapplan.mock.TestContainer;
import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.domain.PlanCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlanServiceImplTest {

    TestContainer testContainer;
    @BeforeEach
    void init(){
        testContainer = TestContainer.builder()
                .clockHolder(new TestClockProvider(1L))
                .uuidHolder(() -> "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .build();
        Member member1 = Member.builder()
                .id(3L)
                .email("test3@naver.com")
                .password("test333")
                .phone("010-2222-2722")
                .name("테스트333")
                .memberRole(MemberRole.MEMBER)
                .memberStatus(MemberStatus.ACTIVE)
                .statusMessage("안녕하세요?")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .modifiedAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .build();
        Member member2 = Member.builder()
                .id(4L)
                .email("test@naver.com")
                .password("test333")
                .phone("010-2222-2722")
                .name("테스트333")
                .memberRole(MemberRole.MEMBER)
                .memberStatus(MemberStatus.ACTIVE)
                .statusMessage("안녕하세요?")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .modifiedAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .build();
        testContainer.memberRepository.saveMember(member1);
        testContainer.memberRepository.saveMember(member2);

    }

    @Test
    @DisplayName("Service 를 통해 plan 을 저장합니다. ")
    void PlanServiceImplSaveTest() {


        //Given
        ArrayList<String> roles = new ArrayList<>();
        roles.add(MemberRole.MEMBER.toString());

        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(3L, "test3@naver.com", roles, new TestClockProvider(Instant.now().toEpochMilli()));


        accessToken = "Bearer "+accessToken;
        PlanCreate planCreate = PlanCreate.builder()
                .title("test 입니다.")
                .content("내용입니다.")
                .appointmentDate(new TestClockProvider(9L).clockProvider())
                .category("카테고리입니다.")
                .location("서울입니다.")
                .build();
        //When
        Plan plan = testContainer
                .planService
                .savePlan(planCreate, accessToken);

        //Then
        assertThat(planCreate.getTitle()).isEqualTo(plan.getTitle());
        assertThat(planCreate.getContent()).isEqualTo(plan.getContent());
        assertThat(planCreate.getAppointmentDate()).isEqualTo(plan.getAppointmentDate());
        assertThat(planCreate.getCategory()).isEqualTo(plan.getCategory());
        assertThat(planCreate.getLocation()).isEqualTo(plan.getLocation());
        assertThat(plan.getCreatedAt()).isEqualTo(new TestClockProvider(1L).clockProvider());
        assertThat(plan.getModifiedAt()).isEqualTo(new TestClockProvider(1L).clockProvider());
    }

    @Test
    @DisplayName("planId로 plan 을 조회한다.")
    void findPlanDetail() {
        //Given
        ArrayList<String> roles = new ArrayList<>();
        roles.add(MemberRole.MEMBER.toString());

        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(3L, "test3@naver.com", roles, new TestClockProvider(Instant.now().toEpochMilli()));


        accessToken = "Bearer "+accessToken;
        PlanCreate planCreate = PlanCreate.builder()
                .title("test 입니다.")
                .content("내용입니다.")
                .appointmentDate(new TestClockProvider(9L).clockProvider())
                .category("카테고리입니다.")
                .location("서울입니다.")
                .build();
        Plan plan = testContainer
                .planService
                .savePlan(planCreate, accessToken);
        //When
        Plan getPlan = testContainer.planService.findPlan(plan.getId(), accessToken);

        //Then
        assertThat(getPlan.getId()).isEqualTo(plan.getId());
        assertThat(getPlan.getAuthor()).isEqualTo(plan.getAuthor());
        assertThat(getPlan.getCreatedAt()).isEqualTo(plan.getCreatedAt());


    }

    @Test
    @DisplayName("작성자와 다른 아이디면 접근이 불가능 합니다.")
    void UnauthorizedServiceExceptionPlanDetail() {
        //Given
        ArrayList<String> roles = new ArrayList<>();
        roles.add(MemberRole.MEMBER.toString());

        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(3L, "test3@naver.com", roles, new TestClockProvider(Instant.now().toEpochMilli()));


        accessToken = "Bearer "+accessToken;
        PlanCreate planCreate = PlanCreate.builder()
                .title("test 입니다.")
                .content("내용입니다.")
                .appointmentDate(new TestClockProvider(9L).clockProvider())
                .category("카테고리입니다.")
                .location("서울입니다.")
                .build();
        Plan plan = testContainer
                .planService
                .savePlan(planCreate, accessToken);
        //When
        String anotherAccessToken = testContainer
                .jwtTokenizer
                .createAccessToken(4L, "test@naver.com", roles, new TestClockProvider(Instant.now().toEpochMilli()));
        anotherAccessToken = "Bearer "+anotherAccessToken;
        //Then
        String finalAccessToken = anotherAccessToken;

        assertThatThrownBy(()->testContainer.planService
                .findPlan(plan.getId(), finalAccessToken))
                .isInstanceOf(UnauthorizedServiceException.class);


    }
}