package com.mapwithplan.mapplan.plan.service.port;

import com.mapwithplan.mapplan.plan.domain.Plan;

public interface PlanRepository {

    Plan save(Plan plan);
}
