package com.mapwithplan.mapplan.post.domain;

import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestClockHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PostImgTest {

    @Test
    @DisplayName("PostImg 도메인을 만들 수 있다.")
    void PostImgTest() {
        //Given
        String uploadName = "upload.test";
        String storeName = "store.test";
        PostCreate postCreate = PostCreate.builder()
                .title("Post")
                .anonymousName("아무 이름")
                .content("아무 내용")
                .location("서울")
                .build();
        Member member = Member.builder()
                .name("test 이름")
                .build();
        //When
        Post post = Post.from(postCreate, member, new TestClockHolder(1L));

        //When
        PostImg postImg = PostImg
                .from(post, uploadName, storeName, new TestClockHolder(1L));
        //Then
        assertThat(postImg.getPost()).isEqualTo(post);
        assertThat(postImg.getStoreFileName()).isEqualTo(storeName);
        assertThat(postImg.getUploadFileName()).isEqualTo(uploadName);
        assertThat(postImg.getRegistrationDate()).isEqualTo(new TestClockHolder(1L).clockHold());

    }

}