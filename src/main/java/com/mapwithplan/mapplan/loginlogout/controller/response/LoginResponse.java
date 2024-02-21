package com.mapwithplan.mapplan.loginlogout.controller.response;

import com.mapwithplan.mapplan.member.domain.MemberRole;
import com.mapwithplan.mapplan.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final String email;


    private final MemberRole memberRole;

    private final String accessToken;
    private final String refreshToken;


    @Builder
    public LoginResponse(String email, MemberRole memberRole, String accessToken, String refreshToken) {
        this.email = email;
        this.memberRole = memberRole;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResponse from(Member member, String accessToken,String refreshToken){
        return LoginResponse.builder()
                .email(member.getEmail())
                .memberRole(member.getMemberRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


}
