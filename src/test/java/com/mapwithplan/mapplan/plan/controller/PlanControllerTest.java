package com.mapwithplan.mapplan.plan.controller;

import com.mapwithplan.mapplan.member.domain.EMemberRole;
import com.mapwithplan.mapplan.member.domain.EMemberStatus;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestClockHolder;
import com.mapwithplan.mapplan.mock.TestContainer;
import com.mapwithplan.mapplan.plan.controller.response.PlanCreateResponse;
import com.mapwithplan.mapplan.plan.controller.response.PlanDetailResponse;
import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.domain.PlanCreate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PlanControllerTest {

    TestContainer testContainer;

    @BeforeEach
    void init(){
        testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(1L))
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
    @DisplayName("계획을 저장할 수 있습니다.")
    void PlanControllerSaveTest() {

        ArrayList<String> roles = new ArrayList<>();
        roles.add(EMemberRole.MEMBER.toString());
        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(3L, "test3@naver.com", roles, new TestClockHolder(Instant.now().toEpochMilli()));
        //Given
        accessToken = "Bearer "+accessToken;
        PlanCreate planCreate = PlanCreate.builder()
                .title("test 입니다.")
                .content("내용입니다.")
                .appointmentDate(new TestClockHolder(9L).clockHold())
                .category("카테고리입니다.")
                .location("서울입니다.")
                .build();

        //When
        ResponseEntity<PlanCreateResponse> plan = testContainer
                .planController
                .createPlan(accessToken,planCreate);

        //Then
        assertThat(plan.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(plan.getBody().getTitle()).isEqualTo(planCreate.getTitle());
        assertThat(plan.getBody().getLocation()).isEqualTo(planCreate.getLocation());
        assertThat(plan.getBody().getContent()).isEqualTo(planCreate.getContent());
        assertThat(plan.getBody().getCategory()).isEqualTo(planCreate.getCategory());
        assertThat(plan.getBody().getAppointmentDate()).isEqualTo(planCreate.getAppointmentDate());
        assertThat(plan.getBody().getAuthorName()).isEqualTo("테스트333");



    }

    @Test
    @DisplayName("PlanDetailResponse 을 응답 받는다.")
    void planDetailPlanControllerTest() {
        //Given
        ArrayList<String> roles = new ArrayList<>();
        roles.add(EMemberRole.MEMBER.toString());
        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(3L, "test3@naver.com", roles, new TestClockHolder(Instant.now().toEpochMilli()));
        accessToken = "Bearer "+accessToken;
        PlanCreate planCreate = PlanCreate.builder()
                .title("test 입니다.")
                .content("내용입니다.")
                .appointmentDate(new TestClockHolder(9L).clockHold())
                .category("카테고리입니다.")
                .location("서울입니다.")
                .build();
        Plan plan = testContainer
                .planService.savePlan(planCreate, accessToken);

        //When
        ResponseEntity<PlanDetailResponse> planDetail = testContainer.planController.planDetail(accessToken, plan.getId());
        //Then
        assertThat(planDetail.getStatusCode())
                .isEqualTo(HttpStatusCode.valueOf(200));

        assertThat(planDetail.getBody().getTitle())
                .isEqualTo(planCreate.getTitle());

        assertThat(planDetail.getBody().getAppointmentDate())
                .isEqualTo(planCreate.getAppointmentDate());
        assertThat(planDetail.getBody().getLocation())
                .isEqualTo(planCreate.getLocation());
        assertThat(planDetail.getBody().getContent())
                .isEqualTo(planCreate.getContent());
        assertThat(planDetail.getBody().getCategory())
                .isEqualTo(planCreate.getCategory());


    }

}