package com.mapwithplan.mapplan.PlanShareFriendship.domain;


import com.mapwithplan.mapplan.friendship.domain.FriendStatus;
import com.mapwithplan.mapplan.friendship.domain.Friendship;
import com.mapwithplan.mapplan.member.domain.Member;

import com.mapwithplan.mapplan.mock.TestClockProvider;
import com.mapwithplan.mapplan.plan.domain.Plan;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PlanShareFriendshipTest {

    @Test
    @DisplayName("PlanShareFriendshipCreated 으로 PlanShareFriendship 도메인을 생성한다.")
    void PlanShareFriendshipCreatedPlanShareFriendshipTest() {
        //Given
        Member member = Member.builder()
                .name("test123").build();
        Plan plan = Plan.builder()
                .id(1L)
                .title("test")
                .content("testContent")
                .author(member)
                .location("서울")
                .modifiedAt(new TestClockProvider(1L).clockProvider())
                .createdAt(new TestClockProvider(1L).clockProvider())
                .build();

        Member friend = Member.builder()
                .name("friend").build();

        Friendship friendship = Friendship.builder()
                .id(1L)
                .friendMemberId(friend)
                .memberId(member)
                .friendshipDate(new TestClockProvider(1L).clockProvider())
                .friendNickName(friend.getName())
                .friendStatus(FriendStatus.ACTIVE)
                .build();

        PlanShareFriendshipCreate planShareFriendshipCreate = PlanShareFriendshipCreate.builder()
                .friendship(friendship)
                .plan(plan)
                .build();

        //When
        PlanShareFriendship planShareFriendship = PlanShareFriendship
                .from(planShareFriendshipCreate, new TestClockProvider(5L));

        //Then

        assertThat(planShareFriendship.getFriendship()).isEqualTo(planShareFriendshipCreate.getFriendship());
        assertThat(planShareFriendship.getPlan()).isEqualTo(planShareFriendshipCreate.getPlan());
        assertThat(planShareFriendship.getCreatedAt()).isEqualTo(new TestClockProvider(5L).clockProvider());
        assertThat(planShareFriendship.getModifiedAt()).isEqualTo(new TestClockProvider(5L).clockProvider());

    }

}