package com.mapwithplan.mapplan.post.domain;

import com.mapwithplan.mapplan.common.aop.logparameteraop.annotation.LogInputTrace;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 게시글과 해당 게시글의 이미지를 담는 도메인 입니다.
 */
@Getter
public class PostDetail {

    private Post post;

    private List<PostImg> postImgList;

    @Builder(toBuilder = true)
    public PostDetail(Post post, List<PostImg> postImgList) {
        this.post = post;
        this.postImgList = postImgList;
    }

    /**
     * 게시글 내용만 담아서 생성하는 메서드 입니다.
     * @param post 해당 게시글 입니다.
     * @return
     */
    public static PostDetail from(Post post){
        return PostDetail.builder()
                .post(post)
                .build();
    }

    /**
     * 이미지가 존재할시 추가로 도메인에 이미지 정보를 담는 메서드 입니다.
     * @param postDetail 현재 도메인입니다.
     * @param postImgList 추가할 이미지 리스트 입니다.
     * @return
     */
    public PostDetail addPostImg(PostDetail postDetail,List<PostImg> postImgList){
        return postDetail.toBuilder()
                .postImgList(postImgList)
                .build();
    }
}
