package com.mapwithplan.mapplan.PlanShareFriendship.domain;

import com.mapwithplan.mapplan.friendship.domain.Friendship;
import com.mapwithplan.mapplan.plan.domain.Plan;
import lombok.Builder;
import lombok.Getter;

/**
 * 공유된 일정과 친구에게 공유할때 생성을 위한 DTO 입니다.
 */
@Getter
public class PlanShareFriendshipCreate {

    private Friendship friendship;

    private Plan plan;
    @Builder
    public PlanShareFriendshipCreate(Friendship friendship, Plan plan) {
        this.friendship = friendship;
        this.plan = plan;
    }
}
