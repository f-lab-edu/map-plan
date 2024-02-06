package com.mapwithplan.mapplan.plan.service;


import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
import com.mapwithplan.mapplan.plan.controller.port.PlanService;
import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.domain.PlanCreate;
import com.mapwithplan.mapplan.plan.service.port.PlanRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Builder
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final TimeClockHolder clockHolder;

    @Override
    public Plan savePlan(PlanCreate planCreate) {
        Plan plan = Plan.from(planCreate, clockHolder);
        return  planRepository.save(plan);
    }
}
