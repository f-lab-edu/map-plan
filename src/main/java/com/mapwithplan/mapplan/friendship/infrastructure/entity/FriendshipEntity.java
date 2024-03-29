package com.mapwithplan.mapplan.friendship.infrastructure.entity;

import com.mapwithplan.mapplan.friendship.domain.FriendStatus;
import com.mapwithplan.mapplan.friendship.domain.Friendship;
import com.mapwithplan.mapplan.member.infrastructure.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Table(name = "friendship")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FriendshipEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendship_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberId;

    @ManyToOne
    @JoinColumn(name = "friend_member_id")
    private MemberEntity friendMemberId;

    @Column(name = "friend_nickname")
    private String friendNickname;

    @Column(name = "friendship_date")
    private LocalDateTime friendshipDate;

    @Column(name = "friend_status")
    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;

    @Builder
    public FriendshipEntity(Long id, MemberEntity memberId, MemberEntity friendMemberId, String friendNickname, LocalDateTime friendshipDate, FriendStatus friendStatus) {
        this.id = id;
        this.memberId = memberId;
        this.friendMemberId = friendMemberId;
        this.friendNickname = friendNickname;
        this.friendshipDate = friendshipDate;
        this.friendStatus = friendStatus;
    }




    public static FriendshipEntity from(Friendship friendship){
        return FriendshipEntity.builder()
                .id(friendship.getId())
                .friendMemberId(MemberEntity.from(friendship.getFriendMemberId()))
                .friendshipDate(friendship.getFriendshipDate())
                .friendNickname(friendship.getFriendNickName())
                .memberId(MemberEntity.from(friendship.getMemberId()))
                .friendStatus(friendship.getFriendStatus())
                .build();
    }

    public Friendship toModel(){
        return Friendship.builder()
                .id(id)
                .memberId(memberId.toModel())
                .friendNickName(friendNickname)
                .friendMemberId(friendMemberId.toModel())
                .friendshipDate(friendshipDate)
                .friendStatus(friendStatus)
                .build();
    }

    public void approve(){
        this.friendStatus = FriendStatus.ACTIVE;
    }
}
