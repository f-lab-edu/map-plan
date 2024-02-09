package com.mapwithplan.mapplan.PlanShareFriendship.controller.port;

import com.mapwithplan.mapplan.PlanShareFriendship.controller.response.PlanShareFriendshipResponse;
import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendship;
import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendshipList;

import java.util.List;


public interface PlanShareFriendshipService {


    List<PlanShareFriendship> sharePlan(Long planId, PlanShareFriendshipList planShareFriendshipList);
}
