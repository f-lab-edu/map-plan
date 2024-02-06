package com.mapwithplan.mapplan.friendship.domain;

import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
import com.mapwithplan.mapplan.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class Friendship {

    private Long id;

    private Member memberId;

    private Member friendMemberId;

    private String friendNickName;

    private LocalDateTime friendshipDate;
    private EfriendStatus efriendStatus;

    @Builder
    public Friendship(Long id, Member memberId, Member friendMemberId, String friendNickName, LocalDateTime friendshipDate, EfriendStatus efriendStatus) {
        this.id = id;
        this.memberId = memberId;
        this.friendMemberId = friendMemberId;
        this.friendNickName = friendNickName;
        this.friendshipDate = friendshipDate;
        this.efriendStatus = efriendStatus;
    }




    public static Friendship from(FriendshipCreate friendshipCreate, TimeClockHolder clockHolder){
        return Friendship.builder()
                .memberId(friendshipCreate.getMemberId())
                .friendMemberId(friendshipCreate.getFriendMemberId())
                .friendNickName(friendshipCreate.getFriendNickName())
                .efriendStatus(EfriendStatus.PENDING)
                .friendshipDate(clockHolder.clockHold())
                .build();

    }


}
