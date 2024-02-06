package com.mapwithplan.mapplan.plan.domain;

import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestClockHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PlanTest {

    @Test
    @DisplayName("Plan 도메인을 PlanCreate 로 만든다.")
    void fromPlanCreate() {
        PlanCreate planCreate = PlanCreate.builder()
                .title("test 입니다.")
                .content("내용입니다.")
                .appointmentDate(new TestClockHolder(1L).clockHold())
                .category("카테고리입니다.")
                .location("서울입니다.")
                .build();
        Member member = Member.builder()
                .id(1L)
                .name("test").build();
        Plan plan = Plan.from(planCreate,member,new TestClockHolder(9L));


        assertThat(planCreate.getTitle()).isEqualTo(plan.getTitle());
        assertThat(planCreate.getContent()).isEqualTo(plan.getContent());
        assertThat(planCreate.getAppointmentDate()).isEqualTo(plan.getAppointmentDate());
        assertThat(planCreate.getCategory()).isEqualTo(plan.getCategory());
        assertThat(planCreate.getLocation()).isEqualTo(plan.getLocation());



    }
}