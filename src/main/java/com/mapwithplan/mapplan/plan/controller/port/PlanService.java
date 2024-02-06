package com.mapwithplan.mapplan.plan.controller.port;

import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.domain.PlanCreate;

public interface PlanService {


    Plan savePlan(PlanCreate planCreate);
}
