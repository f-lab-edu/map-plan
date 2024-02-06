package com.mapwithplan.mapplan.plan.service;


import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
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
    private final TimeClockHolder clockHolder;
    private final JwtTokenizer jwtTokenizer;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public Plan savePlan(PlanCreate planCreate, String authorizationHeader) {
        Member emailAndFindMember = getEmailAndFindMember(authorizationHeader);

        Plan plan = Plan.from(planCreate,emailAndFindMember, clockHolder);
        return  planRepository.save(plan);
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
