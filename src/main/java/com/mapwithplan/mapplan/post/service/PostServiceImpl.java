package com.mapwithplan.mapplan.post.service;


import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockProvider;
import com.mapwithplan.mapplan.member.controller.port.MemberService;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.post.controller.port.PostService;
import com.mapwithplan.mapplan.post.domain.Post;
import com.mapwithplan.mapplan.post.domain.PostCreate;
import com.mapwithplan.mapplan.post.service.port.PostRepository;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Builder
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final TimeClockProvider clockProvider;

    private final MemberService memberService;


    /**
     * 게시글을 생성합니다. 헤더에 있는 토큰 값을 활용하여 회원 정보를 찾고
     * 게시글을 생성합니다.
     * @param postCreate 게시글 생성에 필요한 DTO 입니다.
     * @param authorizationHeader 헤더에 있는 토큰 값 입니다.
     * @return 생성된 게시글을 리턴 합니다.
     */
    @Transactional
    @Override
    public Post createPost(PostCreate postCreate, String authorizationHeader) {
        Member member = memberService.findByEmailUseAccessToken(authorizationHeader);
        Post post = Post.from(postCreate, member, clockProvider);
        return postRepository.createPost(post);
    }


}
