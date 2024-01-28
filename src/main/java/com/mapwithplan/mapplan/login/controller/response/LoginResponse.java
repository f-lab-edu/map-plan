package com.mapwithplan.mapplan.login.controller.response;

import com.mapwithplan.mapplan.member.domain.EMemberType;
import com.mapwithplan.mapplan.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private String email;

    private String token;
    private EMemberType memberType;
    @Builder
    public LoginResponse(String email, String token, EMemberType memberType) {
        this.email = email;
        this.token = token;
        this.memberType = memberType;
    }




    public static LoginResponse from(Member member, String token){
        return LoginResponse.builder()
                .email(member.getEmail())
                .memberType(member.getEMemberType())
                .token(token)
                .build();
    }
}
