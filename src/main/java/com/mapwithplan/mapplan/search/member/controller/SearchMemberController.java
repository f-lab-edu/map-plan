package com.mapwithplan.mapplan.search.member.controller;


import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.search.member.controller.port.SearchMemberService;
import com.mapwithplan.mapplan.search.member.controller.response.FindMemberResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Builder
@RequiredArgsConstructor
@RestController
@RequestMapping("/search/member")
public class SearchMemberController {

    private final SearchMemberService searchMemberService;

    /**
     * 요청 받은 이메일을 기반으로 회원을 조회합니다.
     * 조회된 회원은 FindMemberResponse 에 있는 정보만을 반환합니다.
     * @param email 요청받은 메일입니다.
     * @return 검색 요청 받은 email 을 기반으로 값을 return 합니다.
     */
    @GetMapping("/email")
    public ResponseEntity<List<FindMemberResponse>>  searchMemberByEmail(@RequestParam String email){
        List<FindMemberResponse> findMemberResponses = searchMemberService
                .findMemberByEmail(email)
                .stream()
                .map(FindMemberResponse::from)
                .toList();
        return ResponseEntity.ok()
                .body(findMemberResponses);
    }
}
