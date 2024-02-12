package com.mapwithplan.mapplan.post.domain;

import com.mapwithplan.mapplan.common.timeutils.domain.BaseTime;
import com.mapwithplan.mapplan.common.timeutils.infrastructure.ClockHolder;
import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
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

    private EPostStatus ePostStatus;

    @Builder
    public Post(LocalDateTime createdAt, LocalDateTime modifiedAt, Long id, Member member, String title, String content, String anonymousName, Integer countLike, String location, EPostStatus ePostStatus) {
        super(createdAt, modifiedAt);
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
        this.anonymousName = anonymousName;
        this.countLike = countLike;
        this.location = location;
        this.ePostStatus = ePostStatus;
    }

    public static Post from(PostCreate postCreate , Member member, TimeClockHolder clockHolder){

        final Integer DEFAULT_LIKE = 0;

        return Post.builder()
                .member(member)
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .anonymousName(postCreate.getAnonymousName())
                .location(postCreate.getLocation())
                .createdAt(clockHolder.clockHold())
                .modifiedAt(clockHolder.clockHold())
                .countLike(DEFAULT_LIKE)
                .ePostStatus(EPostStatus.ACTIVE)
                .build();
    }
}
