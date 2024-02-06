package com.mapwithplan.mapplan.plan.service;

import com.mapwithplan.mapplan.member.domain.EMemberRole;
import com.mapwithplan.mapplan.member.domain.EMemberStatus;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestClockHolder;
import com.mapwithplan.mapplan.mock.TestContainer;
import com.mapwithplan.mapplan.mock.planmock.TestPlanContainer;
import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.domain.PlanCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PlanServiceImplTest {

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
    @DisplayName("Service 를 통해 plan 을 저장합니다. ")
    void PlanServiceImplSaveTest() {


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
        assertThat(plan.getCreatedAt()).isEqualTo(new TestClockHolder(1L).clockHold());
        assertThat(plan.getModifiedAt()).isEqualTo(new TestClockHolder(1L).clockHold());
    }


}