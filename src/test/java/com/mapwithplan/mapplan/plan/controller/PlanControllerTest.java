package com.mapwithplan.mapplan.plan.controller;

import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestClockHolder;
import com.mapwithplan.mapplan.mock.planmock.TestPlanContainer;
import com.mapwithplan.mapplan.plan.controller.response.PlanCreateResponse;
import com.mapwithplan.mapplan.plan.domain.PlanCreate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PlanControllerTest {

    @Test
    @DisplayName("계획을 저장할 수 있습니다.")
    void PlanControllerSaveTest() {
        TestPlanContainer testPlanContainer = TestPlanContainer.builder()
                .clockHolder(new TestClockHolder(2L))
                .build();

        //Given
        PlanCreate planCreate = PlanCreate.builder()
                .title("test 입니다.")
                .content("내용입니다.")
                .appointmentDate(new TestClockHolder(9L).clockHold())
                .author(Member.builder()
                        .id(1L)
                        .name("작가입니다.").build())
                .category("카테고리입니다.")
                .location("서울입니다.")
                .build();

        //When
        ResponseEntity<PlanCreateResponse> plan = testPlanContainer
                .planController
                .createPlan(planCreate);

        //Then
        assertThat(plan.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(plan.getBody().getAuthorName()).isEqualTo(planCreate.getAuthor().getName());
        assertThat(plan.getBody().getTitle()).isEqualTo(planCreate.getTitle());
        assertThat(plan.getBody().getLocation()).isEqualTo(planCreate.getLocation());
        assertThat(plan.getBody().getContent()).isEqualTo(planCreate.getContent());
        assertThat(plan.getBody().getCategory()).isEqualTo(planCreate.getCategory());
        assertThat(plan.getBody().getAppointmentDate()).isEqualTo(planCreate.getAppointmentDate());



    }

}