package com.mapwithplan.mapplan.post.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapwithplan.mapplan.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

/**
 * 게시글 생성에 필요한 DTO 입니다.
 */
@Getter
public class PostCreate {


    private String title;

    private String content;

    private String anonymousName;

    private String location;

    @Builder
    public PostCreate(@JsonProperty("title") String title,
                      @JsonProperty("content") String content,
                      @JsonProperty("anonymousName") String anonymousName,
                      @JsonProperty("location") String location) {
        this.title = title;
        this.content = content;
        this.anonymousName = anonymousName;
        this.location = location;
    }
}
