package com.mapwithplan.mapplan.friendship.domain;

import com.mapwithplan.mapplan.member.domain.Member;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

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
