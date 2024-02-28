package com.mapwithplan.mapplan.post.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

/**
 * 게시글 생성에 필요한 DTO 입니다.
 */
@Getter
public class PostRequest {

    @NotEmpty
    @Size(min = 1, max = 20)
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    @Size(min = 1, max = 10)
    private String anonymousName;

    @NotEmpty
    private String location;

    @Builder
    public PostRequest(@JsonProperty("title") String title,
                       @JsonProperty("content") String content,
                       @JsonProperty("anonymousName") String anonymousName,
                       @JsonProperty("location") String location) {
        this.title = title;
        this.content = content;
        this.anonymousName = anonymousName;
        this.location = location;
    }
}
