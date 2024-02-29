package com.mapwithplan.mapplan.post.service;


import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockProvider;
import com.mapwithplan.mapplan.common.uuidutils.service.port.UuidHolder;
import com.mapwithplan.mapplan.member.controller.port.MemberService;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.post.controller.port.PostService;
import com.mapwithplan.mapplan.post.domain.*;
import com.mapwithplan.mapplan.post.service.port.PostImgRepository;
import com.mapwithplan.mapplan.post.service.port.PostRepository;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;


@Slf4j
@Builder
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final TimeClockProvider clockProvider;

    private final MemberService memberService;

    private final UuidHolder uuidHolder;

    private final PostImgRepository postImgRepository;

    private final FileService fileService;

    /**
     * 헤더에 있는 토큰 값을 활용하여 회원 정보를 찾고 게시글을 생성합니다.
     * 게시글에 파일이 존재하지 않을 경우 게시글 내용만 저장하고
     * 게스글에 파일이 존재한다면 이미지 파일도 저장한다.
     * 이미지 파일을 저장하게 되었을 때 이미지 메타 데이터를  postImgRepository.saveAll(postImgList)를 사용해 함께 저장한다.
     * @param postRequest 게시글 생성에 필요한 DTO 입니다.
     * @param authorizationHeader 헤더에 있는 토큰 값 입니다.
     * @param postImgFiles 게시글에 첨부된 파일이다. required = false 는 해당 파라미터가 필수는 아님을 나타낸다.
     * @return 생성된 게시글 정보와 이미지 정보를 리턴 합니다.
     */
    @Override
    @Transactional
    public Post createPost(PostRequest postRequest, List<MultipartFile> postImgFiles, String authorizationHeader){
        Member member = memberService.findByEmailUseAccessToken(authorizationHeader);
        List<PostImg> postImgList = fileService.uploadFilesToObjectStorage(postImgFiles, uuidHolder);
        Post post = Post.from(postRequest, postImgList, member, clockProvider);

        return postRepository.createPost(post);
    }



    @Transactional(readOnly = true)
    @Override
    public Resource findFile(String filename) {

        InputStream inputStream = fileService.downloadImage(filename);


        return new InputStreamResource(inputStream);


    }


}
