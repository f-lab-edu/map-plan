package com.mapwithplan.mapplan.post.controller;


import com.mapwithplan.mapplan.post.controller.port.PostService;
import com.mapwithplan.mapplan.post.domain.Post;

import com.mapwithplan.mapplan.post.domain.PostRequest;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
     * @param postRequest 게시글에 대한 정보를 담고 있는 DTO 입니다.
     * @param postImgFiles 게시글에 첨부된 파일이다. required = false 는 해당 파라미터가 필수는 아님을 나타낸다.
     * @return PostCreateResponse 에 게시글 정보는 기본적으로 담겨 있고 이미지 파일이 있다면 이미지 정보까지 담아서 리턴한다.
     */
    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestHeader("Authorization") String authorizationHeader,
                                                         @RequestPart(name = "PostCreate" ) @Validated PostRequest postRequest,
                                                         @RequestPart(name = "postImgFiles",required = false) List<MultipartFile> postImgFiles){

        Post post = postService.createPost(postRequest, postImgFiles, authorizationHeader);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(post);
    }


    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable String filename) {

        Resource resource = postService.findFile( filename);

        String contentDisposition = "attachment; filename=\"" + filename + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition)
                .body(resource);

    }
}
