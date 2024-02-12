package com.mapwithplan.mapplan.post.controller.response;


import com.mapwithplan.mapplan.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 게시글 생성 후 controller 에서 return 해줄 때 사용하는 response 입니다.
 */
@Getter
public class PostCreateResponse {

    private String title;

    private String content;

    private String anonymousName;

    private Integer countLike;

    private String location;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
    @Builder
    public PostCreateResponse(String title, String content, String anonymousName, Integer countLike, String location, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.title = title;
        this.content = content;
        this.anonymousName = anonymousName;
        this.countLike = countLike;
        this.location = location;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static PostCreateResponse from(Post post){
        return PostCreateResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .anonymousName(post.getAnonymousName())
                .countLike(post.getCountLike())
                .location(post.getLocation())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
    }
}
