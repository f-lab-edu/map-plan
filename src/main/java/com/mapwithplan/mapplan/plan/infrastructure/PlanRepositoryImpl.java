package com.mapwithplan.mapplan.plan.infrastructure;

import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.infrastructure.entity.PlanEntity;
import com.mapwithplan.mapplan.plan.service.port.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * plan 과 연관된 repository class 입니다.
 */
@RequiredArgsConstructor
@Repository
public class PlanRepositoryImpl implements PlanRepository {


    private final PlanJPARepository planJPARepository;

    /**
     * 일정에 대한 저장을 진행합니다.
     * @param plan
     * @return
     */
    @Override
    public Plan save(Plan plan) {
        return planJPARepository.save(PlanEntity.from(plan)).toModel();
    }

    @Override
    public Plan findPlanDetail(Long planId) {
        return planJPARepository
                .findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", planId))
                .toModel();
    }
}
