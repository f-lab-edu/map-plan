package com.mapwithplan.mapplan.member.controller.response;


import com.mapwithplan.mapplan.member.domain.Member;
import lombok.Getter;

@Getter
public class MemberMyPageResponse {

    private final String email;

    private final String statusMessage;


    public MemberMyPageResponse(String email, String statusMessage) {
        this.email = email;
        this.statusMessage = statusMessage;
    }

    public static MemberMyPageResponse from(Member member){
        return new MemberMyPageResponse(member.getEmail(), member.getStatusMessage());
    }
}
