package com.mapwithplan.mapplan.post.controller;


import com.mapwithplan.mapplan.post.controller.port.PostService;
import com.mapwithplan.mapplan.post.controller.response.PostCreateResponse;
import com.mapwithplan.mapplan.post.domain.PostCreate;
import com.mapwithplan.mapplan.post.domain.PostDetail;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Builder
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    /**
     * API 를 통해 게시글에 대한 정보를 전달 받는다. 전달 내용에는 게시글의 내용, 게시글에 첨부할 이미지가 담겨온다.
     * @param authorizationHeader 회원 정보를 확인하기 위해 필요한 파라미터
     * @param postCreate 게시글에 대한 정보를 담고 있는 DTO 입니다.
     * @param postImgFiles 게시글에 첨부된 파일이다. required = false 는 해당 파라미터가 필수는 아님을 나타낸다.
     * @return PostCreateResponse 에 게시글 정보는 기본적으로 담겨 있고 이미지 파일이 있다면 이미지 정보까지 담아서 리턴한다.
     */
    @PostMapping("/create")
    public ResponseEntity<PostCreateResponse> createPost(@RequestHeader("Authorization") String authorizationHeader,
                                                         @RequestPart(name = "PostCreate" ) @Validated PostCreate postCreate,
                                                         @RequestPart(name = "postImgFiles",required = false) List<MultipartFile> postImgFiles){
        PostDetail post = postService.createPost(postCreate, postImgFiles, authorizationHeader);
        PostCreateResponse response = PostCreateResponse.from(post.getPost());
        if (post.getPostImgList() != null){
            response = response.addPostImgList(response, post.getPostImgList());
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }
}
