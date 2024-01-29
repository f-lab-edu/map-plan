package com.mapwithplan.mapplan.login.controller.response;

import com.mapwithplan.mapplan.member.domain.EMemberType;
import com.mapwithplan.mapplan.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final String email;


    private final EMemberType memberType;

    private final String accessToken;
    private final String refreshToken;


    @Builder
    public LoginResponse(String email, EMemberType memberType, String accessToken, String refreshToken) {
        this.email = email;
        this.memberType = memberType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResponse from(Member member, String accessToken,String refreshToken){
        return LoginResponse.builder()
                .email(member.getEmail())
                .memberType(member.getEMemberType())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
