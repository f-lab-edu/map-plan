package com.mapwithplan.mapplan.member.controller.response;

import com.mapwithplan.mapplan.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberCreateResponse {

    private String email;

    
    public static MemberCreateResponse from(Member member){
        return MemberCreateResponse.builder()
                .email(member.getEmail())
                .build();
    }
}
