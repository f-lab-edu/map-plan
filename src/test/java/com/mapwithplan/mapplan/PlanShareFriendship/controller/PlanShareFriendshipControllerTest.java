package com.mapwithplan.mapplan.PlanShareFriendship.controller;

import com.mapwithplan.mapplan.PlanShareFriendship.controller.response.PlanShareFriendshipResponse;
import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendshipList;
import com.mapwithplan.mapplan.friendship.domain.EFriendStatus;
import com.mapwithplan.mapplan.friendship.domain.Friendship;
import com.mapwithplan.mapplan.member.domain.EMemberRole;
import com.mapwithplan.mapplan.member.domain.EMemberStatus;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestClockHolder;
import com.mapwithplan.mapplan.mock.TestContainer;
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
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PlanShareFriendshipControllerTest {

    TestContainer testContainer;

    String accessToken;

    Long planId;
    List<Long> friendshipsIds = new ArrayList<>();
    @BeforeEach
    void init(){
        testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(1L))
                .uuidHolder(() -> "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .build();
        Member role = Member.builder()
                .id(1L)
                .email("test@naver.com")
                .password("test")
                .phone("010-2222-0000")
                .name("테스트" )
                .eMemberRole(EMemberRole.MEMBER)
                .memberStatus(EMemberStatus.ACTIVE)
                .statusMessage("안녕하세요?" )
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 1, 24, 12, 1))
                .modifiedAt(LocalDateTime.of(2024, 1, 24, 12, 1))
                .build();
        testContainer.memberRepository.saveMember(role);

        for (int i = 1; i < 10; i++) {
            Member member = Member.builder()
                    .id(1L+i)
                    .email("test+"+i+"@naver.com")
                    .password("test"+i)
                    .phone("010-2222-000"+i)
                    .name("테스트" +i)
                    .eMemberRole(EMemberRole.MEMBER)
                    .memberStatus(EMemberStatus.ACTIVE)
                    .statusMessage("안녕하세요?" + i)
                    .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                    .createdAt(LocalDateTime.of(2024, 1, 24, 12, i))
                    .modifiedAt(LocalDateTime.of(2024, 1, 24, 12, i))
                    .build();
            Friendship friendship = Friendship.builder()
                    .id(i + 1L)
                    .friendMemberId(member)
                    .memberId(role)
                    .friendNickName(member.getName())
                    .efriendStatus(EFriendStatus.ACTIVE)
                    .build();
            friendshipsIds.add(i + 1L);
            testContainer.friendshipRepository.createFriendship(friendship);
            testContainer.memberRepository.saveMember(member);
        }


        //Given
        ArrayList<String> roles = new ArrayList<>();
        roles.add(EMemberRole.MEMBER.toString());

        accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(1L, "test@naver.com", roles, new TestClockHolder(Instant.now().toEpochMilli()));


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
        planId = plan.getId();
    }
    @Test
    @DisplayName("친구들과 일정을 공유합니다.")
    void sharePlanWithFriendshipTest() {
        //Given
        List<Long> test = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            test.add((long) i);
        }

        PlanShareFriendshipList build = PlanShareFriendshipList.builder()
                .friendshipsIds(test).build();

        //When
        ResponseEntity<PlanShareFriendshipResponse> responseEntity = testContainer
                .planShareFriendshipController
                .sharePlanWithFriendship(planId, build);
        List<String> friendshipNames = responseEntity.getBody().getFriendshipNames();

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(responseEntity.getBody().getPlanId()).isEqualTo(planId);
        long i = 1L;
        for (String friendshipName : friendshipNames) {
            assertThat(friendshipName).isEqualTo("테스트"+i);
            i = i+ 1L;
        }
    }
}