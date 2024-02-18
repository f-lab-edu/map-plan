package com.mapwithplan.mapplan.post.controller.response;


import com.mapwithplan.mapplan.common.aop.logparameteraop.annotation.LogInputTrace;
import com.mapwithplan.mapplan.post.domain.Post;
import com.mapwithplan.mapplan.post.domain.PostImg;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시글 생성 후 controller 에서 return 해줄 때 사용하는 response 입니다.
 */
@Getter
public class PostCreateResponse {

    private Long id;
    private String title;

    private String content;

    private String anonymousName;

    private Integer countLike;

    private String location;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private List<PostImgResponse> postImgResponses;
    @Builder(toBuilder = true)
    public PostCreateResponse(Long id, String title, String content, String anonymousName, Integer countLike, String location, LocalDateTime createdAt, LocalDateTime modifiedAt, List<PostImgResponse> postImgResponses) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.anonymousName = anonymousName;
        this.countLike = countLike;
        this.location = location;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.postImgResponses = postImgResponses;
    }

    public static PostCreateResponse from(Post post){
        return PostCreateResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .anonymousName(post.getAnonymousName())
                .countLike(post.getCountLike())
                .location(post.getLocation())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
    }
    @LogInputTrace
    public PostCreateResponse addPostImgList(PostCreateResponse response, List<PostImg> postImgList){
        return response.toBuilder()
                .postImgResponses(postImgList
                        .stream()
                        .map(PostImgResponse::toResponse)
                        .toList())
                .build();
    }
}
