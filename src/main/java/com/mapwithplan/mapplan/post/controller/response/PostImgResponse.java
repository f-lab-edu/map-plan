package com.mapwithplan.mapplan.post.controller.response;

import com.mapwithplan.mapplan.post.domain.Post;
import com.mapwithplan.mapplan.post.domain.PostImg;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostImgResponse {

    private String uploadFileName;

    private String storeFileName;

    @Builder
    public PostImgResponse(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }


    public static PostImgResponse toResponse(PostImg postImg){
        return PostImgResponse.builder()
                .uploadFileName(postImg.getUploadFileName())
                .storeFileName(postImg.getStoreFileName())
                .build();
    }
}
