package com.mapwithplan.mapplan.PlanShareFriendship.service.port;

import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendship;

import java.util.List;

public interface PlanShareFriendshipRepository {


    /**
     *
     * PlanShareFriendship 이 담긴 리스트를 한번에 저장합니다.
     * @param planShareFriendships PlanShareFriendship 이 담긴 리스트 객체 입니다.
     * @return
     */
    List<PlanShareFriendship> saveAllSharePlan(List<PlanShareFriendship> planShareFriendships);

}
