package com.mapwithplan.mapplan.plan.service.port;

import com.mapwithplan.mapplan.plan.domain.Plan;

import java.util.Optional;


/**
 * service class 에 사용되는 PlanRepository 인테페이스입니다.
 * PlanRepositoryImpl 에서 implement 를 진행했습니다.
 */
public interface PlanRepository {

    /**
     * 회원에 대한 저장을 진행하는 메서드 입니다.
     * @param plan
     * @return
     */
    Plan save(Plan plan);

    /**
     * 회원이 작성한 글의 상세 내용을 조회합니다.
     * @param planId
     * @return
     */
    Plan findPlanDetail(Long planId);


}
