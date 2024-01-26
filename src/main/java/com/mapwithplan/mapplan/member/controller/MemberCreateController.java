package com.mapwithplan.mapplan.member.controller;


import com.mapwithplan.mapplan.member.controller.port.MemberService;
import com.mapwithplan.mapplan.member.controller.response.MemberCreateResponse;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.domain.MemberCreate;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Builder
@Tag(name= "회원(member)")
@RestController
@RequestMapping("/member/create")
public class MemberCreateController {

    private final MemberService memberService;

    public MemberCreateController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberCreateResponse> memberCreate(@Validated @RequestBody MemberCreate memberCreate){
        log.info("memberCreateController 시작");
        Member member = memberService.saveMember(memberCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MemberCreateResponse.from(member));
    }


}
