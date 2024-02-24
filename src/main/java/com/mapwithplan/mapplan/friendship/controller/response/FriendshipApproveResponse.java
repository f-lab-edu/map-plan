package com.mapwithplan.mapplan.friendship.controller.response;

import lombok.Getter;

/**
 * 친구 승인시 보주는 응답 Response 입니다. 승인한 친구의 이름을 출력합니다.
 */
@Getter
public class FriendshipApproveResponse {

    private final String friendName;


    public FriendshipApproveResponse(String friendName) {
        this.friendName = friendName;
    }


}
