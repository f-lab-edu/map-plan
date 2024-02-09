package com.mapwithplan.mapplan.PlanShareFriendship.domain;

import com.mapwithplan.mapplan.common.timeutils.domain.BaseTime;
import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
import com.mapwithplan.mapplan.friendship.domain.Friendship;
import com.mapwithplan.mapplan.plan.domain.Plan;
import jakarta.validation.ClockProvider;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


/**
 * Plan 과 Friendship 중간에 있는 도메인 역할을 합니다.
 */

@Getter
public class PlanShareFriendship extends BaseTime {


    private Long id;

    private Friendship friendship;

    private Plan plan;



    @Builder
    public PlanShareFriendship(LocalDateTime createdAt, LocalDateTime modifiedAt, Long id, Friendship friendship, Plan plan ){
        super(createdAt, modifiedAt);
        this.id = id;
        this.friendship = friendship;
        this.plan = plan;
    }

    public static PlanShareFriendship from(PlanShareFriendshipCreate planShareFriendshipCreate, TimeClockHolder timeClockHolder){
        return PlanShareFriendship.builder()
                .plan(planShareFriendshipCreate.getPlan())
                .friendship(planShareFriendshipCreate.getFriendship())
                .modifiedAt(timeClockHolder.clockHold())
                .createdAt(timeClockHolder.clockHold())
                .build();
    }

}
