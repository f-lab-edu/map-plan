package com.mapwithplan.mapplan.friendship.controller;

import com.mapwithplan.mapplan.friendship.controller.response.FriendshipApplyResponse;
import com.mapwithplan.mapplan.friendship.controller.response.FriendshipApproveResponse;
import com.mapwithplan.mapplan.friendship.domain.Friendship;
import com.mapwithplan.mapplan.friendship.domain.FriendshipCreate;
import com.mapwithplan.mapplan.member.domain.EMemberRole;
import com.mapwithplan.mapplan.member.domain.EMemberStatus;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestClockHolder;
import com.mapwithplan.mapplan.mock.TestContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class FriendshipControllerTest {


    TestContainer testContainer;
    @BeforeEach
    void init(){
        this.testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(1L))
                .build();

        Member member1 = Member.builder()
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
                .build();
        Member member2 = Member.builder()
                .id(4L)
                .email("test@naver.com")
                .password("test333")
                .phone("010-2222-2722")
                .name("테스트333")
                .eMemberRole(EMemberRole.MEMBER)
                .memberStatus(EMemberStatus.ACTIVE)
                .statusMessage("안녕하세요?")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .createdAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .modifiedAt(LocalDateTime.of(2024, 1, 24, 12, 30))
                .build();
        testContainer.memberRepository.saveMember(member1);
        testContainer.memberRepository.saveMember(member2);
    }

    @Test
    @DisplayName("applyFriendship controller 테스트 승인에 성공합니다.")
    void applyFriendshipFriendshipControllerTest() {
        ArrayList<String> roles = new ArrayList<>();
        roles.add(EMemberRole.MEMBER.toString());
        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(3L, "test3@naver.com", roles, new TestClockHolder(Instant.now().toEpochMilli()));
        accessToken = "Bearer "+accessToken;
        //Given
        //When
        ResponseEntity<FriendshipApplyResponse> response = testContainer
                .friendshipController
                .applyFriendship(accessToken, new FriendshipCreate(4L));

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));

        assertThat(response.getBody().getApplyName()).isEqualTo("테스트333");
        assertThat(response.getBody().getFriendshipId()).isNotNull();

    }

    @Test
    @DisplayName("approveFriendship controller 테스트 승인에 성공합니다.")
    void approveFriendshipFriendshipControllerTest() {
        //Given
        ArrayList<String> roles = new ArrayList<>();
        roles.add(EMemberRole.MEMBER.toString());
        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(4L, "test@naver.com", roles, new TestClockHolder(Instant.now().toEpochMilli()));
        accessToken = "Bearer "+accessToken;

        //When
        testContainer
                .friendshipController
                .applyFriendship(accessToken, new FriendshipCreate(3L));

        ResponseEntity<FriendshipApproveResponse> response = testContainer
                .friendshipController
                .approveFriendship(1L);

        //Then

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody().getFriendName())
                .isEqualTo(new FriendshipApproveResponse("테스트333").getFriendName());
    }
}