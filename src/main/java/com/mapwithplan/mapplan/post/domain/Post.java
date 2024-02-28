package com.mapwithplan.mapplan.post.domain;

import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.common.timeutils.domain.BaseTime;

import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockProvider;
import com.mapwithplan.mapplan.member.domain.Member;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Post  extends BaseTime {


    private Long id;

    private Member member;

    private String title;

    private String content;

    private String anonymousName;

    private Integer countLike;

    private String location;

    private PostStatus postStatus;

    @Builder
    public Post(LocalDateTime createdAt, LocalDateTime modifiedAt, Long id, Member member, String title, String content, String anonymousName, Integer countLike, String location, PostStatus postStatus) {
        super(createdAt, modifiedAt);
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
        this.anonymousName = anonymousName;
        this.countLike = countLike;
        this.location = location;
        this.postStatus = postStatus;
    }

    public static Post from(PostRequest postRequest, Member member, TimeClockProvider timeClockProvider){

        final Integer DEFAULT_LIKE = 0;
        if (member == null){
            throw new ResourceNotFoundException("member","회원 정보");
        }

        return Post.builder()
                .member(member)
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .anonymousName(postRequest.getAnonymousName())
                .location(postRequest.getLocation())
                .createdAt(timeClockProvider.clockProvider())
                .modifiedAt(timeClockProvider.clockProvider())
                .countLike(DEFAULT_LIKE)
                .postStatus(PostStatus.ACTIVE)
                .build();
    }
}
