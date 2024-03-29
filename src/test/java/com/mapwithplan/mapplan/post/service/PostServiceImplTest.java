package com.mapwithplan.mapplan.post.service;


import com.mapwithplan.mapplan.member.domain.Member;

import com.mapwithplan.mapplan.member.domain.MemberRole;
import com.mapwithplan.mapplan.mock.TestClockProvider;
import com.mapwithplan.mapplan.mock.TestContainer;
import com.mapwithplan.mapplan.mock.TestUuidHolder;
import com.mapwithplan.mapplan.mock.postmock.FakeMultipartFile;

import com.mapwithplan.mapplan.post.domain.PostRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class PostServiceImplTest {

    TestContainer testContainer;

    Member member;
    @BeforeEach
    void init(){
        this.testContainer = TestContainer.builder()
                .uuidHolder(new TestUuidHolder("hj-this-is-uuid"))
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
        List<MultipartFile> testFile = new ArrayList<>();
        FakeMultipartFile fakeMultipartFile = FakeMultipartFile.builder()
                .originalFilename("test.png")
                .name("test.png").build();

        testFile.add(fakeMultipartFile);

        //When


        //Then

    }




}