package com.mapwithplan.mapplan.friendship.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapwithplan.mapplan.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

/**
 * 친구 요청시 필요한 DTO 입니다.
 * 친구의 회원 ID 만을 받아서 사용합니다.
 */
@Getter
public class FriendshipCreate {


    private Long friendMemberId;

    @Builder
    public FriendshipCreate(@JsonProperty("friendMemberId") Long friendMemberId) {

        this.friendMemberId = friendMemberId;

    }
}
