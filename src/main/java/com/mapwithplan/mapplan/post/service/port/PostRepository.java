package com.mapwithplan.mapplan.post.service.port;

import com.mapwithplan.mapplan.post.domain.Post;

/**
 * Port 역할을 하는 interface 입니다.
 */
public interface PostRepository {

    /**
     * 게시글 생성에 필요한 메서드 입니다.
     * service 클래스에서 사용되는 메서드 입니다.
     * @param post 생성된 게시글을 저장합니다.
     * @return 저장된 게시글을 리턴 합니다.
     */
    Post createPost(Post post);


    Post findPostWithImagesById(Long postId);
}
