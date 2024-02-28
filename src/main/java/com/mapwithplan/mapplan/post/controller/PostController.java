package com.mapwithplan.mapplan.post.controller;


import com.mapwithplan.mapplan.post.controller.port.PostService;
import com.mapwithplan.mapplan.post.controller.response.PostCreateResponse;
import com.mapwithplan.mapplan.post.domain.Post;
import com.mapwithplan.mapplan.post.domain.PostRequest;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@Builder
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    /**
     * 게시글 생성에 필요한 메서드 입니다.
     * @param authorizationHeader 헤더에 있는 토큰을 활용하기 위해 RequestHeader 를 사용합니다.
     * @param postRequest 게시글 생성에 필요한 값을 받아오는 DTO 입니다.
     * @return 생성된 게시글을 PostCreateResponse 에 담아 리턴 합니다.
     */
    @PostMapping("/create")
    public ResponseEntity<PostCreateResponse> createPost(@RequestHeader("Authorization") String authorizationHeader,
                                                         @RequestBody @Validated PostRequest postRequest){
        Post post = postService.createPost(postRequest, authorizationHeader);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PostCreateResponse.from(post));

    }
}
