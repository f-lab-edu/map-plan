package com.mapwithplan.mapplan.post.controller;


import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.domain.MemberRole;

import com.mapwithplan.mapplan.mock.TestClockProvider;
import com.mapwithplan.mapplan.mock.TestContainer;

import com.mapwithplan.mapplan.post.domain.PostRequest;
import com.mapwithplan.mapplan.mock.TestUuidHolder;
import com.mapwithplan.mapplan.mock.postmock.FakeMultipartFile;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostControllerTest {

    TestContainer testContainer;

    Member member;
    @BeforeEach
    void init(){
        this.testContainer = TestContainer.builder()
                .uuidHolder(new TestUuidHolder("testtsetset"))
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
        PostRequest postRequest = PostRequest.builder()
                .title("Post")
                .anonymousName("아무 이름")
                .content("아무 내용")
                .location("서울")
                .build();
        //When
        List<MultipartFile> testFile = null;

//        ResponseEntity<Post> post = testContainer.postController.createPost(accessToken, postCreate,testFile);
//        //Then
//        assertThat(post.getBody().getAnonymousName()).isEqualTo(postCreate.getAnonymousName());
//        assertThat(post.getBody().getTitle()).isEqualTo(postCreate.getTitle());
//        assertThat(post.getBody().getCreatedAt()).isEqualTo(new TestClockProvider(1L).clockProvider());
//        assertThat(post.getBody().getLocation()).isEqualTo(postCreate.getLocation());
//        assertThat(post.getBody().getCountLike()).isEqualTo(0);
//        assertThat(post.getBody().getModifiedAt()).isEqualTo(new TestClockProvider(1L).clockProvider());
//        assertThat(post.getBody().getContent()).isEqualTo(postCreate.getContent());

    }


    @Test
    @DisplayName("이미지를 포함한 createPost 를 진행한다.")
    void PostServiceImplCreatePostAddImgTest() {

        ArrayList<String> roles = new ArrayList<>();
        roles.add(MemberRole.MEMBER.toString());

        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(1L, "test@test.com", roles, new TestClockProvider(Instant.now().toEpochMilli()));


        accessToken = "Bearer "+accessToken;
        //Given
        PostRequest postCreate = PostRequest.builder()
                .title("Post")
                .anonymousName("아무 이름")
                .content("아무 내용")
                .location("서울")
                .build();
        //When
        FakeMultipartFile fakeMultipartFile = FakeMultipartFile.builder()
                .name("test.png")
                .originalFilename("originalFilename.png")
                .build();
        List<MultipartFile> testFiles = new ArrayList<>();
        testFiles.add(fakeMultipartFile);

//        ResponseEntity<Post> post = testContainer.postController.createPost(accessToken, postCreate,testFiles);
//        //Then
//        assertThat(post.getBody().getAnonymousName()).isEqualTo(postCreate.getAnonymousName());
//        assertThat(post.getBody().getTitle()).isEqualTo(postCreate.getTitle());
//        assertThat(post.getBody().getCreatedAt()).isEqualTo(new TestClockProvider(1L).clockProvider());
//        assertThat(post.getBody().getLocation()).isEqualTo(postCreate.getLocation());
//        assertThat(post.getBody().getCountLike()).isEqualTo(0);
//        assertThat(post.getBody().getModifiedAt()).isEqualTo(new TestClockProvider(1L).clockProvider());
//        assertThat(post.getBody().getContent()).isEqualTo(postCreate.getContent());

    }

}