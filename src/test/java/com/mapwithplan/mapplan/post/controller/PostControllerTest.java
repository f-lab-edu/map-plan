package com.mapwithplan.mapplan.post.controller;

import com.mapwithplan.mapplan.member.domain.EMemberRole;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestClockHolder;
import com.mapwithplan.mapplan.mock.TestContainer;
import com.mapwithplan.mapplan.mock.TestUuidHolder;
import com.mapwithplan.mapplan.mock.postmock.FakeMultipartFile;
import com.mapwithplan.mapplan.post.controller.response.PostCreateResponse;
import com.mapwithplan.mapplan.post.domain.Post;
import com.mapwithplan.mapplan.post.domain.PostCreate;
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
                .clockHolder(new TestClockHolder(1L))
                .uuidHolder(new TestUuidHolder("testtsetset")).build();
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
        roles.add(EMemberRole.MEMBER.toString());

        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(1L, "test@test.com", roles, new TestClockHolder(Instant.now().toEpochMilli()));


        accessToken = "Bearer "+accessToken;
        //Given
        PostCreate postCreate = PostCreate.builder()
                .title("Post")
                .anonymousName("아무 이름")
                .content("아무 내용")
                .location("서울")
                .build();
        //When
        List<MultipartFile> testFile = null;

        ResponseEntity<PostCreateResponse> post = testContainer.postController.createPost(accessToken, postCreate,testFile);
        //Then
        assertThat(post.getBody().getAnonymousName()).isEqualTo(postCreate.getAnonymousName());
        assertThat(post.getBody().getTitle()).isEqualTo(postCreate.getTitle());
        assertThat(post.getBody().getCreatedAt()).isEqualTo(new TestClockHolder(1L).clockHold());
        assertThat(post.getBody().getLocation()).isEqualTo(postCreate.getLocation());
        assertThat(post.getBody().getCountLike()).isEqualTo(0);
        assertThat(post.getBody().getModifiedAt()).isEqualTo(new TestClockHolder(1L).clockHold());
        assertThat(post.getBody().getContent()).isEqualTo(postCreate.getContent());

    }


    @Test
    @DisplayName("이미지를 포함한 createPost 를 진행한다.")
    void PostServiceImplCreatePostAddImgTest() {

        ArrayList<String> roles = new ArrayList<>();
        roles.add(EMemberRole.MEMBER.toString());

        String accessToken = testContainer
                .jwtTokenizer
                .createAccessToken(1L, "test@test.com", roles, new TestClockHolder(Instant.now().toEpochMilli()));


        accessToken = "Bearer "+accessToken;
        //Given
        PostCreate postCreate = PostCreate.builder()
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

        ResponseEntity<PostCreateResponse> post = testContainer.postController.createPost(accessToken, postCreate,testFiles);
        //Then
        assertThat(post.getBody().getAnonymousName()).isEqualTo(postCreate.getAnonymousName());
        assertThat(post.getBody().getTitle()).isEqualTo(postCreate.getTitle());
        assertThat(post.getBody().getCreatedAt()).isEqualTo(new TestClockHolder(1L).clockHold());
        assertThat(post.getBody().getLocation()).isEqualTo(postCreate.getLocation());
        assertThat(post.getBody().getCountLike()).isEqualTo(0);
        assertThat(post.getBody().getModifiedAt()).isEqualTo(new TestClockHolder(1L).clockHold());
        assertThat(post.getBody().getContent()).isEqualTo(postCreate.getContent());
        assertThat(post.getBody().getPostImgResponses().size()).isEqualTo(1);
        assertThat(post.getBody().getPostImgResponses().stream().findFirst().get().getUploadFileName())
                .isEqualTo(fakeMultipartFile.getOriginalFilename());
        assertThat(post.getBody().getPostImgResponses().stream().findFirst().get().getStoreFileName())
                .isEqualTo("testtsetset.png");

    }

}