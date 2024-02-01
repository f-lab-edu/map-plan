package com.mapwithplan.mapplan.loginlogout.controller.response;

import com.mapwithplan.mapplan.member.domain.EMemberRole;
import com.mapwithplan.mapplan.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final String email;


    private final EMemberRole memberType;

    private final String accessToken;
    private final String refreshToken;


    @Builder
    public LoginResponse(String email, EMemberRole memberType, String accessToken, String refreshToken) {
        this.email = email;
        this.memberType = memberType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResponse from(Member member, String accessToken,String refreshToken){
        return LoginResponse.builder()
                .email(member.getEmail())
                .memberType(member.getEMemberRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


}
