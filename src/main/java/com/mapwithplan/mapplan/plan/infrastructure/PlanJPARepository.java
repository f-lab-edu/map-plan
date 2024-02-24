package com.mapwithplan.mapplan.plan.infrastructure;

import com.mapwithplan.mapplan.plan.infrastructure.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanJPARepository extends JpaRepository<PlanEntity, Long> {
}
