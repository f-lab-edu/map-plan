package com.mapwithplan.mapplan.friendship.service;

import com.mapwithplan.mapplan.common.exception.UnauthorizedServiceException;
import com.mapwithplan.mapplan.friendship.domain.FriendStatus;
import com.mapwithplan.mapplan.friendship.domain.Friendship;
import com.mapwithplan.mapplan.friendship.domain.FriendshipCreate;

import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.domain.MemberRole;
import com.mapwithplan.mapplan.member.domain.MemberStatus;

import com.mapwithplan.mapplan.mock.TestClockProvider;
import com.mapwithplan.mapplan.mock.TestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

class FriendshipServiceImplTest {

    TestContainer testContainer;

    @BeforeEach
    void init(){
        this.testContainer = TestContainer.builder()
                .clockHolder(new TestClockProvider(1L))
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
        Member member3 = Member.builder()
                .id(5L)
                .email("test4@naver.com")
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
        testContainer.memberRepository.saveMember(member3);
    }


    @Test
    @DisplayName("applyFriendship 로 친구 승인을 요청한다.")
    void applyFriendshipTest() {
        ArrayList<String> roles = new ArrayList<>();
        roles.add(MemberRole.MEMBER.toString());

        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(3L, "test3@naver.com", roles, new TestClockProvider(Instant.now().toEpochMilli()));
        //Given
        FriendshipCreate friendshipCreate = new FriendshipCreate(4L);

        //When
        Friendship friendship = testContainer
                .friendshipService
                .applyFriendship(friendshipCreate, accessToken);

        //Then
        assertThat(friendship.getFriendshipDate()).isEqualTo(new TestClockProvider(1L).clockProvider());
        assertThat(friendship.getId()).isEqualTo(1L);
        assertThat(friendship.getFriendStatus()).isEqualTo(FriendStatus.PENDING);
    }
    @Test
    @DisplayName("applyFriendship 로 친구 승인을 요청한다.")
    void approveFriendship() {
        ArrayList<String> roles = new ArrayList<>();
        roles.add(MemberRole.MEMBER.toString());

        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(3L, "test3@naver.com", roles, new TestClockProvider(Instant.now().toEpochMilli()));
        //Given
        FriendshipCreate friendshipCreate = new FriendshipCreate(4L);

        //When
        Friendship friendship = testContainer
                .friendshipService
                .applyFriendship(friendshipCreate, accessToken);

        accessToken = "Bearer "+accessToken;
        Friendship approve = testContainer
                .friendshipService.approveFriendship(accessToken, friendship.getId());
        //Then

        assertThat(approve.getFriendStatus()).isEqualTo(FriendStatus.ACTIVE);
    }

    @Test
    @DisplayName("잘못된 계정으로 승인 요청 시 applyFriendship 이 거절 된다.")
    void rejectFriendship() {
        ArrayList<String> roles = new ArrayList<>();
        roles.add(MemberRole.MEMBER.toString());

        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(3L, "test3@naver.com", roles, new TestClockProvider(Instant.now().toEpochMilli()));
        //Given
        FriendshipCreate friendshipCreate = new FriendshipCreate(4L);
        String rejectToken = testContainer
                .jwtTokenizer
                .createAccessToken(5L, "test4@naver.com", roles, new TestClockProvider(Instant.now().toEpochMilli()));
        //When
        Friendship friendship = testContainer
                .friendshipService
                .applyFriendship(friendshipCreate, accessToken);

        accessToken = "Bearer "+accessToken;

        //Then
        assertThatThrownBy(()->testContainer
                .friendshipService.approveFriendship(rejectToken, friendship.getId())).isInstanceOf(UnauthorizedServiceException.class);

    }
}