package com.mapwithplan.mapplan.post.controller;


import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.domain.MemberRole;

import com.mapwithplan.mapplan.mock.TestClockProvider;
import com.mapwithplan.mapplan.mock.TestContainer;
import com.mapwithplan.mapplan.post.controller.response.PostCreateResponse;
import com.mapwithplan.mapplan.post.domain.Post;
import com.mapwithplan.mapplan.post.domain.PostCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostControllerTest {

    TestContainer testContainer;

    Member member;
    @BeforeEach
    void init(){
        this.testContainer = TestContainer.builder()
                .clockHolder(new TestClockProvider(1L)).build();
        member = Member.builder()
                .name("test 이름")
                .phone("010-1234-1234")
                .email("test@test.com")
                .build();
        testContainer.memberRepository.saveMember(member);

    }


    @Test
    @DisplayName("createPost 를 진행한다.")
    void PostServiceImplCreatePostTest() {

        ArrayList<String> roles = new ArrayList<>();
        roles.add(MemberRole.MEMBER.toString());

        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(1L, "test@test.com", roles, new TestClockProvider(Instant.now().toEpochMilli()));


        accessToken = "Bearer "+accessToken;
        //Given
        PostCreate postCreate = PostCreate.builder()
                .title("Post")
                .anonymousName("아무 이름")
                .content("아무 내용")
                .location("서울")
                .build();
        //When

        ResponseEntity<PostCreateResponse> post = testContainer.postController.createPost(accessToken, postCreate);
        //Then
        assertThat(post.getBody().getAnonymousName()).isEqualTo(postCreate.getAnonymousName());
        assertThat(post.getBody().getTitle()).isEqualTo(postCreate.getTitle());
        assertThat(post.getBody().getCreatedAt()).isEqualTo(new TestClockProvider(1L).clockProvider());
        assertThat(post.getBody().getLocation()).isEqualTo(postCreate.getLocation());
        assertThat(post.getBody().getCountLike()).isEqualTo(0);
        assertThat(post.getBody().getModifiedAt()).isEqualTo(new TestClockProvider(1L).clockProvider());
        assertThat(post.getBody().getContent()).isEqualTo(postCreate.getContent());

    }

}