package com.mapwithplan.mapplan.member.controller.response;


import com.mapwithplan.mapplan.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberMyPageDetailResponse {

    private final String email;

    private final String statusMessage;

    private final String phone;

    @Builder
    public MemberMyPageDetailResponse(String email, String statusMessage, String phone) {
        this.email = email;
        this.statusMessage = statusMessage;
        this.phone = phone;
    }

    public static MemberMyPageDetailResponse from(Member member){
        return MemberMyPageDetailResponse.builder()
                .email(member.getEmail())
                .phone(member.getPhone())
                .statusMessage(member.getStatusMessage())
                .build();
    }
}
