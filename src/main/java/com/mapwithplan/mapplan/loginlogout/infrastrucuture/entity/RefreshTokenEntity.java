package com.mapwithplan.mapplan.loginlogout.infrastrucuture.entity;


import com.mapwithplan.mapplan.loginlogout.domain.RefreshToken;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.infrastructure.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 회원 한명당 1개의 리프레시 토큰만 가질 수 있게 할 것이므로 RefreshToken과 Member를 1:1 연관관계를 맺어줍니다.
 * 그리고 연관된 회원ID를 외래키 겸 기본키로 지정해주며,
 * 재발급 횟수를 제한할 것이기 때문에 리프레시 토큰마다 재발급 횟수를 저장할 프로퍼티도 추가한 엔티티입니다.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="refresh_token")
public class RefreshTokenEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    private String token;

    @Builder
    public RefreshTokenEntity (Long id, MemberEntity memberEntity, String token) {
        this.id = id;
        this.memberEntity = memberEntity;
        this.token = token;
    }

    public static RefreshTokenEntity from(RefreshToken refreshToken){
        return RefreshTokenEntity.builder()
                .memberEntity(MemberEntity.from(refreshToken.getMember()))
                .token(refreshToken.getToken())
                .build();
    }

    public RefreshToken toModel(){
        return RefreshToken.builder()
                .id(id)
                .member(memberEntity.toModel())
                .token(token)
                .build();
    }


    public void updateToken(String refreshToken){
        this.token = refreshToken;
    }
}
