package com.mapwithplan.mapplan.plan.controller;


import com.mapwithplan.mapplan.plan.controller.port.PlanService;
import com.mapwithplan.mapplan.plan.controller.response.PlanCreateResponse;
import com.mapwithplan.mapplan.plan.controller.response.PlanDetailResponse;
import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.domain.PlanCreate;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Builder
@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;


    /**
     * 회원을 생성하고 성공시 PlanCreateResponse 를 리턴합니다.
     * @param authorizationHeader accessToken 입니다.
     * @param planCreate 일정 생성에 사용되는 DTO 입니다.
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<PlanCreateResponse> createPlan(@RequestHeader("Authorization") String authorizationHeader,
                                                         @RequestBody PlanCreate planCreate){
        Plan plan = planService.savePlan(planCreate, authorizationHeader);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PlanCreateResponse.from(plan));
    }

    /**
     * 회원이 작성한 글을 조회합니다.
     * @param authorizationHeader
     * @param planId
     * @return
     */
    @GetMapping("/{planId}")
    public ResponseEntity<PlanDetailResponse> planDetail(@RequestHeader("Authorization") String authorizationHeader,
                                                         @PathVariable("planId") Long planId){
        Plan plan = planService.findPlan(planId, authorizationHeader);

        return ResponseEntity.status(HttpStatus.OK)
                .body(PlanDetailResponse.from(plan));
    }
}
