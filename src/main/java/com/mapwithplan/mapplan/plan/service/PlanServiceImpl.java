package com.mapwithplan.mapplan.plan.service;


import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.common.exception.UnauthorizedServiceException;

import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockProvider;
import com.mapwithplan.mapplan.jwt.util.JwtTokenizer;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.service.port.MemberRepository;
import com.mapwithplan.mapplan.plan.controller.port.PlanService;
import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.domain.PlanCreate;
import com.mapwithplan.mapplan.plan.service.port.PlanRepository;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Builder
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final TimeClockProvider clockHolder;
    private final JwtTokenizer jwtTokenizer;
    private final MemberRepository memberRepository;


    /**
     * accessToken 에 대한 분석을 한 후 회원 객체를 찾아 냅니다.
     * 그 정보를 기반으로 일정에 대한 저장을 진행합니다.
     * @param planCreate
     * @param authorizationHeader
     * @return
     */
    @Transactional
    @Override
    public Plan savePlan(PlanCreate planCreate, String authorizationHeader) {
        Member emailAndFindMember = getEmailAndFindMember(authorizationHeader);

        Plan plan = Plan.from(planCreate,emailAndFindMember, clockHolder);
        return  planRepository.save(plan);
    }

    /**
     * 작성한 일정의 회원과 accessToken 으로 조회한 회원이 일치하지 않다면 접근이 불가능합니다.
     * @param planId 일정 조회에 사용되는 ID 입니다.
     * @param authorizationHeader
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Plan findPlan(Long planId, String authorizationHeader) {

        Plan planDetail = planRepository.findPlanDetail(planId);

        Member member = getEmailAndFindMember(authorizationHeader);

        if (!planDetail.getAuthor().getEmail().equals(member.getEmail())){
            throw new UnauthorizedServiceException("접근 할 수 없는 일정입니다.");
        }

        return planDetail;
    }

    /**
     * 헤더 토큰을 활용해 이메일을 찾아내는 메서드 입니다.
     * @param authorizationHeader
     * @return
     */
    private Member getEmailAndFindMember(String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        Claims claims = jwtTokenizer.parseAccessToken(jwtToken);
        String email = claims.getSubject();
        return memberRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Member", email));
    }
}
