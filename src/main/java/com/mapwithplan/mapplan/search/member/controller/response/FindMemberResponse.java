package com.mapwithplan.mapplan.search.member.controller.response;

import com.mapwithplan.mapplan.member.domain.Member;
import lombok.Builder;

public class FindMemberResponse {

    private final Long id;

    private final String email;

    private final String name;

    private final String statusMessage;

    @Builder
    public FindMemberResponse(Long id,String email, String name, String statusMessage) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.statusMessage = statusMessage;
    }

    public static FindMemberResponse from(Member member){
        return FindMemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .statusMessage(member.getStatusMessage())
                .build();
    }
}
