package com.mapwithplan.mapplan.member.controller;


import com.mapwithplan.mapplan.member.controller.port.MemberService;
import com.mapwithplan.mapplan.member.controller.response.MemberCreateResponse;
import com.mapwithplan.mapplan.member.controller.response.MemberMyPageDetailResponse;
import com.mapwithplan.mapplan.member.controller.response.MemberMyPageResponse;
import com.mapwithplan.mapplan.member.domain.EditMember;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.domain.MemberCreate;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
@Builder
@Tag(name= "회원(member)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{id}/verify")
    public ResponseEntity<Void> verifyEmail(
            @PathVariable("id") long id,
            @RequestParam("certificationCode") String certificationCode) {
        memberService.verifyEmail(id, certificationCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:3000"))
                .build();
    }

    @PostMapping("/create")
    public ResponseEntity<MemberCreateResponse> memberCreate(@Validated @RequestBody MemberCreate memberCreate){
        Member member = memberService.saveMember(memberCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MemberCreateResponse.from(member));
    }

    @GetMapping("/myPage")
    public ResponseEntity<MemberMyPageResponse> myPage(@RequestHeader("Authorization") String authorizationHeader ){

        Member member = memberService.findByEmailUseAccessToken(authorizationHeader);
        return ResponseEntity.status(HttpStatus.OK)
                .body(MemberMyPageResponse.from(member));

    }

    @GetMapping("/myPage/detail")
    public ResponseEntity<MemberMyPageDetailResponse> myPageDetail(@RequestHeader("Authorization") String authorizationHeader ){

        Member member = memberService.findByEmailUseAccessToken(authorizationHeader);
        return ResponseEntity.status(HttpStatus.OK)
                .body(MemberMyPageDetailResponse.from(member));

    }
    @PatchMapping("/myPage/detail/edit")
    public ResponseEntity<MemberMyPageDetailResponse> myPageDetailEdit(@RequestHeader("Authorization") String authorizationHeader,
                                                                       @RequestBody @Validated EditMember editMember){
        Member editMemberDetail = memberService.editMemberDetail(authorizationHeader,editMember);
        return ResponseEntity.status(HttpStatus.OK)
                .body(MemberMyPageDetailResponse
                        .from(editMemberDetail));

    }

}
