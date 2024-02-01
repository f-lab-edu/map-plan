package com.mapwithplan.mapplan.loginlogout.domain;



import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.infrastructure.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class RefreshToken {


    private Long id;


    private Member member;

    private String token;

    @Builder
    public RefreshToken (Long id, Member member, String token) {
        this.id = id;
        this.member = member;
        this.token = token;
    }

    public static RefreshToken from( Member member, String token){

        return RefreshToken.builder()
                .member(member)
                .token(token)
                .build();
    }

    public void update(String refreshToken){
        this.token = refreshToken;
    }


}
