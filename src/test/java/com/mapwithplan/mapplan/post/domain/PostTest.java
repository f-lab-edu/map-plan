package com.mapwithplan.mapplan.post.domain;

import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.mock.TestClockProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PostTest {

    @Test
    @DisplayName("PostRequest 로 Post 를 만든다.")
    void PostCreateTest() {
        //Given
        PostRequest postRequest = PostRequest.builder()
                .title("Post")
                .anonymousName("아무 이름")
                .content("아무 내용")
                .location("서울")
                .build();
        Member member = Member.builder()
                .name("test 이름")
                .build();
        //When
        Post post = Post.from(postRequest, member, new TestClockProvider(1L));
        //Then

        assertThat(post.getTitle()).isEqualTo(postRequest.getTitle());
        assertThat(post.getAnonymousName()).isEqualTo(postRequest.getAnonymousName());
        assertThat(post.getLocation()).isEqualTo(postRequest.getLocation());
        assertThat(post.getContent()).isEqualTo(postRequest.getContent());
        assertThat(post.getPostStatus()).isEqualTo(PostStatus.ACTIVE);
        assertThat(post.getCreatedAt()).isEqualTo(new TestClockProvider(1L).clockProvider());
        assertThat(post.getModifiedAt()).isEqualTo(new TestClockProvider(1L).clockProvider());
        assertThat(post.getCountLike()).isEqualTo(0);
        assertThat(post.getMember()).isEqualTo(member);

    }

}