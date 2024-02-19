package com.mapwithplan.mapplan.friendship.domain;

import com.mapwithplan.mapplan.member.domain.Member;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

/**
 * 친구 요청시 필요한 DTO 입니다.
 * 친구의 회원 ID 만을 받아서 사용합니다.
 */
@Getter
public class FriendshipCreate {

    @NotEmpty
    private Member memberId;

    @NotEmpty
    private Member friendMemberId;

    private String friendNickName;


    public FriendshipCreate(Member memberId,
                            Member friendMemberId,
                            String friendNickName) {
        this.memberId = memberId;
        this.friendMemberId = friendMemberId;
        this.friendNickName = friendNickName;
    }
}
