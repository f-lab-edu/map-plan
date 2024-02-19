package com.mapwithplan.mapplan.friendship.domain;

import com.mapwithplan.mapplan.member.domain.Member;

import com.mapwithplan.mapplan.mock.TestClockProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FriendshipTest {

    @Test
    @DisplayName("회원 도메인 이용해 friendship 도메인으로 생성합니다.")
    void friendshipFromTest() {
        //Given

        Member member = Member.builder()
                .name("test")
                .id(1L)
                .phone("010-1234-1234")
                .build();
        Member friend = Member.builder()
                .name("test2")
                .id(2L)
                .phone("010-2222-2222")
                .build();
        //When
        Friendship friendship = Friendship.from(friend, member, new TestClockProvider(9L));

        //Then
        Assertions.assertThat(member).isEqualTo(friendship.getMemberId());
        Assertions.assertThat(friendship.getFriendMemberId()).isEqualTo(friend);
        Assertions.assertThat(friend.getName()).isEqualTo(friendship.getFriendNickName());
        Assertions.assertThat(friendship.getFriendStatus()).isEqualTo(FriendStatus.PENDING);
        Assertions.assertThat(friendship.getFriendshipDate()).isEqualTo(new TestClockProvider(9L).clockProvider());
    }
}