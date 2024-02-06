package com.mapwithplan.mapplan.plan.infrastructure;

import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.infrastructure.entity.PlanEntity;
import com.mapwithplan.mapplan.plan.service.port.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PlanRepositoryImpl implements PlanRepository {


    private final PlanJPARepository planJPARepository;


    @Override
    public Plan save(Plan plan) {
        return planJPARepository.save(PlanEntity.from(plan)).toModel();
    }
}
