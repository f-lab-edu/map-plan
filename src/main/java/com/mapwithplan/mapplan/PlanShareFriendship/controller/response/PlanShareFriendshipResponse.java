package com.mapwithplan.mapplan.PlanShareFriendship.controller.response;


import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendship;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;



@Getter
public class PlanShareFriendshipResponse {

    private List<String> friendshipNames;

    private Long planId;

    @Builder
    public PlanShareFriendshipResponse(List<String> friendshipNames, Long planId) {
        this.friendshipNames = friendshipNames;
        this.planId = planId;
    }

    public static PlanShareFriendshipResponse from(Long planId, List<PlanShareFriendship> planShareFriendships){
        List<String> nameList = new ArrayList<>();

        planShareFriendships.forEach(item -> nameList
                .add(item.getFriendship().getFriendNickName()));

        return PlanShareFriendshipResponse.builder()
                .friendshipNames(nameList)
                .planId(planId)
                .build();


    }

}
