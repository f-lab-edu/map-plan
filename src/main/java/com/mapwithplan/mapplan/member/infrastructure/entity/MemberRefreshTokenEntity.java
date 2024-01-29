package com.mapwithplan.mapplan.member.infrastructure.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * 회원 한명당 1개의 리프레시 토큰만 가질 수 있게 할 것이므로 RefreshToken과 Member를 1:1 연관관계를 맺어줍니다.
 * 그리고 연관된 회원ID를 외래키 겸 기본키로 지정해주며,
 * 재발급 횟수를 제한할 것이기 때문에 리프레시 토큰마다 재발급 횟수를 저장할 프로퍼티도 추가한 엔티티입니다.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member_refresh_token")
public class MemberRefreshTokenEntity {

    @Id
    @Column(name= "member_refresh_token_uuid")
    private UUID memberId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    private String refreshToken;
    private int reissueCount = 0;

    public MemberRefreshTokenEntity(MemberEntity memberEntity, String refreshToken) {
        this.memberEntity = memberEntity;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean validateRefreshToken(String refreshToken) {
        return this.refreshToken.equals(refreshToken);
    }

    public void increaseReissueCount() {
        reissueCount++;
    }
}
