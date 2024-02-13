package com.mapwithplan.mapplan.post.service;

import com.mapwithplan.mapplan.member.domain.EMemberRole;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestClockHolder;
import com.mapwithplan.mapplan.mock.TestContainer;
import com.mapwithplan.mapplan.mock.TestUuidHolder;
import com.mapwithplan.mapplan.mock.postmock.FakeMultipartFile;
import com.mapwithplan.mapplan.post.domain.Post;
import com.mapwithplan.mapplan.post.domain.PostCreate;
import com.mapwithplan.mapplan.post.domain.PostDetail;
import com.mapwithplan.mapplan.post.domain.PostImg;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PostServiceImplTest {

    TestContainer testContainer;

    Member member;
    @BeforeEach
    void init(){
        this.testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(1L))
                .uuidHolder(new TestUuidHolder("hj-this-is-uuid"))
                .build();
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
        List<MultipartFile> testFile = new ArrayList<>();
        FakeMultipartFile fakeMultipartFile = FakeMultipartFile.builder()
                .originalFilename("test.png")
                .name("test.png").build();

        testFile.add(fakeMultipartFile);

        //When
        PostDetail post = testContainer.postService.createPost(postCreate, testFile, accessToken);

        //Then
        assertThat(post.getPost().getMember().getEmail()).isEqualTo(member.getEmail());
        assertThat(post.getPostImgList().size()).isEqualTo(1);
    }




}