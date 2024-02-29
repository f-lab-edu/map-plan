package com.mapwithplan.mapplan.post.controller.port;

import com.mapwithplan.mapplan.post.domain.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * controller 와 함께 사용하는 port 역할을 하는 interface 입니다.
 */
public interface PostService {

    /**
     * 게시글 생성에 필요한 메서드 입니다.
     * @param postRequest 게시글 생성시 사용하는 DTO 입니다.
     * @param authorizationHeader 헤더 값에 있는 토큰을 활용해 회원 정보를 확인합니다.
     * @param postImgFiles 게시글에 첨부된 파일이다. required = false 는 해당 파라미터가 필수는 아님을 나타낸다.
     * @return
     */
    Post createPost(PostRequest postRequest, List<MultipartFile> postImgFiles, String authorizationHeader);


    Resource findFile(String filename);

}
