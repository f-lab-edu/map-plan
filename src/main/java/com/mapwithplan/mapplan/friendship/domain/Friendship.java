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
    private EFriendStatus efriendStatus;

    @Builder
    public Friendship(Long id, Member memberId, Member friendMemberId, String friendNickName, LocalDateTime friendshipDate, EFriendStatus efriendStatus) {
        this.id = id;
        this.memberId = memberId;
        this.friendMemberId = friendMemberId;
        this.friendNickName = friendNickName;
        this.friendshipDate = friendshipDate;
        this.efriendStatus = efriendStatus;
    }


    /**
     * 친구 요청이 이루어 질때 생성하는 객체 입니다.
     * 생성이 되었을때 상태는 PENDING 입니다.
     * @param friendMemberId 친구의 정보를 받는 파라미터 입니다.
     * @param member 회원 본인에 대한 파라미터 입니다.
     * @param clockHolder 시간에 대한 값을 기록하기 위한 파라미터 입니다.
     * @return
     */
    public static Friendship from(Member friendMemberId ,Member member, TimeClockHolder clockHolder){
        return Friendship.builder()
                .memberId(member)
                .friendMemberId(friendMemberId)
                .friendNickName(friendMemberId.getName())
                .efriendStatus(EFriendStatus.PENDING)
                .friendshipDate(clockHolder.clockHold())
                .build();
    }

    /**
     * 승인이 이루어질때 EFriendStatus 의 상태가 ACTIVE 로 변경 됩니다.
     */
    public void approve(){
        this.efriendStatus = EFriendStatus.ACTIVE;
    }


}
