package com.mapwithplan.mapplan.post.domain;

import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestClockHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    @DisplayName("PostCreate 로 Post 를 만든다.")
    void PostCreateTest() {
        //Given
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
        //Then

        assertThat(post.getTitle()).isEqualTo(postCreate.getTitle());
        assertThat(post.getAnonymousName()).isEqualTo(postCreate.getAnonymousName());
        assertThat(post.getLocation()).isEqualTo(postCreate.getLocation());
        assertThat(post.getContent()).isEqualTo(postCreate.getContent());
        assertThat(post.getEPostStatus()).isEqualTo(EPostStatus.ACTIVE);
        assertThat(post.getCreatedAt()).isEqualTo(new TestClockHolder(1L).clockHold());
        assertThat(post.getModifiedAt()).isEqualTo(new TestClockHolder(1L).clockHold());
        assertThat(post.getCountLike()).isEqualTo(0);
        assertThat(post.getMember()).isEqualTo(member);

    }

}