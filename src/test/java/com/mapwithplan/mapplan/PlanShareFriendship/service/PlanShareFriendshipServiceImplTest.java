package com.mapwithplan.mapplan.PlanShareFriendship.service;

import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendship;
import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendshipList;

import com.mapwithplan.mapplan.friendship.domain.FriendStatus;
import com.mapwithplan.mapplan.friendship.domain.Friendship;

import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.domain.MemberRole;
import com.mapwithplan.mapplan.member.domain.MemberStatus;

import com.mapwithplan.mapplan.mock.TestClockProvider;
import com.mapwithplan.mapplan.mock.TestContainer;
import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.domain.PlanCreate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanShareFriendshipServiceImplTest {

    TestContainer testContainer;
    String accessToken;

    Long planId;
    List<Long> friendshipsIds = new ArrayList<>();

    @BeforeEach
    void init(){
        testContainer = TestContainer.builder()
                .clockHolder(new TestClockProvider(1L))
                .uuidHolder(() -> "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .build();
        Member role = Member.builder()
                .id(1L)
                .email("test@naver.com")
                .password("test")
                .phone("010-2222-0000")
                .name("테스트" )
                .memberRole(MemberRole.MEMBER)
                .memberStatus(MemberStatus.ACTIVE)
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
                    .memberRole(MemberRole.MEMBER)
                    .memberStatus(MemberStatus.ACTIVE)
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
                    .friendStatus(FriendStatus.ACTIVE)
                    .build();
            friendshipsIds.add(i + 1L);
            testContainer.friendshipRepository.createFriendship(friendship);
            testContainer.memberRepository.saveMember(member);
        }


        //Given
        ArrayList<String> roles = new ArrayList<>();
        roles.add(MemberRole.MEMBER.toString());

        accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(1L, "test@naver.com", roles, new TestClockProvider(Instant.now().toEpochMilli()));


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
        planId = plan.getId();
    }


    @Test
    @DisplayName("일정을 친구들에게 공유할 할 수 있습니다.")
    void sharePlanTest() {


        //Given
        PlanShareFriendshipList build = PlanShareFriendshipList.builder()
                .friendshipsIds(friendshipsIds).build();
        //When
        List<PlanShareFriendship> planShareFriendships = testContainer
                .planShareFriendshipService
                .sharePlan(planId, build, accessToken);

        //Then
        Assertions.assertThat(planShareFriendships.size()).isEqualTo(9);

    }

}