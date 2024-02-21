package com.mapwithplan.mapplan.friendship.controller.response;

import com.mapwithplan.mapplan.friendship.domain.Friendship;
import lombok.Builder;
import lombok.Getter;


/**
 *  친구 요청시 보내주는 Response 입니다.
 *  친구 요청 ID 와 요청한 친구 이름을 출력합니다.
 */
@Getter
public class FriendshipApplyResponse {

    private String applyName;
    private Long friendshipId;

    @Builder
    public FriendshipApplyResponse(String applyName,Long friendshipId) {
        this.applyName = applyName;
        this.friendshipId= friendshipId;
    }

    public static FriendshipApplyResponse from(Friendship friendship){
        return FriendshipApplyResponse.builder()
                .applyName(friendship.getFriendMemberId().getName())
                .friendshipId(friendship.getId()).build();
    }
}
