package com.mapwithplan.mapplan.PlanShareFriendship.controller;

import com.mapwithplan.mapplan.PlanShareFriendship.controller.port.PlanShareFriendshipService;
import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendship;
import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendshipList;
import com.mapwithplan.mapplan.PlanShareFriendship.controller.response.PlanShareFriendshipResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Builder
@RestController
@RequiredArgsConstructor
public class PlanShareFriendshipController {

    private final PlanShareFriendshipService planShareFriendshipService;
    @PostMapping("/plans/share/{planId}")
    public ResponseEntity<PlanShareFriendshipResponse> sharePlanWithFriendship(@PathVariable("planId") Long planId,
                                                                               @RequestBody PlanShareFriendshipList planShareFriendshipList){
        List<PlanShareFriendship> planShareFriendships = planShareFriendshipService.sharePlan(planId, planShareFriendshipList);

        return ResponseEntity.status(HttpStatus.OK)
                .body(PlanShareFriendshipResponse.from(planId,planShareFriendships));
    }
}
