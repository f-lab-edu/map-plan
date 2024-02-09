package com.mapwithplan.mapplan.PlanShareFriendship.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


/**
 * 일정을 친구들에게 공유할때 사용하는 DTO 입니다. 친구들의 ID 를 담고 있습니다.
 */
@Getter
public class PlanShareFriendshipList {

    private final List<Long> friendshipsIds;

    @Builder
    public PlanShareFriendshipList(@JsonProperty("friendshipsIds") List<Long> friendshipsIds) {
        this.friendshipsIds = friendshipsIds;
    }
}
