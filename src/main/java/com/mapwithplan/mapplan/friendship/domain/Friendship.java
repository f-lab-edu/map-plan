package com.mapwithplan.mapplan.friendship.domain;


import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockProvider;
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
    private FriendStatus friendStatus;

    @Builder
    public Friendship(Long id, Member memberId, Member friendMemberId, String friendNickName, LocalDateTime friendshipDate, FriendStatus friendStatus) {
        this.id = id;
        this.memberId = memberId;
        this.friendMemberId = friendMemberId;
        this.friendNickName = friendNickName;
        this.friendshipDate = friendshipDate;
        this.friendStatus = friendStatus;
    }




    public static Friendship from(FriendshipCreate friendshipCreate, TimeClockProvider clockHolder){
        return Friendship.builder()
                .memberId(friendshipCreate.getMemberId())
                .friendMemberId(friendshipCreate.getFriendMemberId())
                .friendNickName(friendshipCreate.getFriendNickName())
                .friendStatus(FriendStatus.PENDING)
                .friendshipDate(clockHolder.clockProvider())
                .build();

    }


}
