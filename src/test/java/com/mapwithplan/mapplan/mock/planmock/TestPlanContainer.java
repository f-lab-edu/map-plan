package com.mapwithplan.mapplan.mock.planmock;

import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;

import com.mapwithplan.mapplan.plan.controller.PlanController;
import com.mapwithplan.mapplan.plan.controller.port.PlanService;
import com.mapwithplan.mapplan.plan.service.PlanServiceImpl;
import com.mapwithplan.mapplan.plan.service.port.PlanRepository;
import lombok.Builder;

public class TestPlanContainer {


    public final PlanRepository planRepository;
    public final PlanService planService;

    public final PlanController planController;

    @Builder
    public TestPlanContainer(TimeClockHolder clockHolder) {
        this.planRepository = new FakePlanRepository();
        this.planService = PlanServiceImpl.builder()
                .planRepository(this.planRepository)
                .clockHolder(clockHolder)
                .build();

        this.planController = PlanController.builder()
                .planService(this.planService)
                .build();
    }
}
