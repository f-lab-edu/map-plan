package com.mapwithplan.mapplan.plan.service;

import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestClockHolder;
import com.mapwithplan.mapplan.mock.planmock.TestPlanContainer;
import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.domain.PlanCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PlanServiceImplTest {

    private TestPlanContainer testPlanContainer;


    @Test
    @DisplayName("Service 를 통해 plan 을 저장합니다. ")
    void PlanServiceImplSaveTest() {
        //Given
        testPlanContainer = TestPlanContainer.builder()
                .clockHolder(new TestClockHolder(1L)).build();
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
        Plan plan = testPlanContainer
                .planService
                .savePlan(planCreate);

        //Then
        assertThat(planCreate.getTitle()).isEqualTo(plan.getTitle());
        assertThat(planCreate.getContent()).isEqualTo(plan.getContent());
        assertThat(planCreate.getAppointmentDate()).isEqualTo(plan.getAppointmentDate());
        assertThat(planCreate.getAuthor()).isEqualTo(plan.getAuthor());
        assertThat(planCreate.getCategory()).isEqualTo(plan.getCategory());
        assertThat(planCreate.getLocation()).isEqualTo(plan.getLocation());
        assertThat(plan.getCreatedAt()).isEqualTo(new TestClockHolder(1L).clockHold());
        assertThat(plan.getModifiedAt()).isEqualTo(new TestClockHolder(1L).clockHold());
    }


}