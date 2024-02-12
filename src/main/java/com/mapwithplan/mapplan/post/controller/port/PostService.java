package com.mapwithplan.mapplan.post.controller.port;

import com.mapwithplan.mapplan.post.domain.Post;
import com.mapwithplan.mapplan.post.domain.PostCreate;

/**
 * controller 와 함께 사용하는 port 역할을 하는 interface 입니다.
 */
public interface PostService {

    /**
     * 게시글 생성에 필요한 메서드 입니다.
     * @param postCreate 게시글 생성시 사용하는 DTO 입니다.
     * @param authorizationHeader 헤더 값에 있는 토큰을 활용해 회원 정보를 확인합니다.
     * @return
     */
    Post createPost(PostCreate postCreate, String authorizationHeader);
}
