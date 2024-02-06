package com.mapwithplan.mapplan.plan.controller;


import com.mapwithplan.mapplan.plan.controller.port.PlanService;
import com.mapwithplan.mapplan.plan.controller.response.PlanCreateResponse;
import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.domain.PlanCreate;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Builder
@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;


    @PostMapping("/create")
    public ResponseEntity<PlanCreateResponse> createPlan(@RequestBody PlanCreate planCreate){
        Plan plan = planService.savePlan(planCreate);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PlanCreateResponse.from(plan));
    }


}
