package com.mapwithplan.mapplan.post.controller.response;


import com.mapwithplan.mapplan.post.domain.PostDetail;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.UrlResource;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostDetailResponse {

    private Long id;
    private String title;

    private String content;

    private String anonymousName;

    private Integer countLike;

    private String location;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private List<UrlResource> urlResourceList;

    @Builder
    public PostDetailResponse(Long id, String title, String content, String anonymousName, Integer countLike, String location, LocalDateTime createdAt, LocalDateTime modifiedAt,List<UrlResource> urlResourceList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.anonymousName = anonymousName;
        this.countLike = countLike;
        this.location = location;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.urlResourceList = urlResourceList;
    }


    public static PostDetailResponse from(PostDetail postDetail){
        return PostDetailResponse.builder()
                .id(postDetail.getPost().getId())
                .title(postDetail.getPost().getTitle())
                .content(postDetail.getPost().getContent())
                .anonymousName(postDetail.getPost().getAnonymousName())
                .countLike(postDetail.getPost().getCountLike())
                .location(postDetail.getPost().getLocation())
                .createdAt(postDetail.getPost().getCreatedAt())
                .modifiedAt(postDetail.getPost().getModifiedAt())
                .build();
    }
}
