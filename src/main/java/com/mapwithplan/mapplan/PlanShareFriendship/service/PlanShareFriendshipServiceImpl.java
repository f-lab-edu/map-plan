package com.mapwithplan.mapplan.PlanShareFriendship.service;


import com.mapwithplan.mapplan.PlanShareFriendship.controller.port.PlanShareFriendshipService;
import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendship;
import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendshipCreate;
import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendshipList;
import com.mapwithplan.mapplan.PlanShareFriendship.service.port.PlanShareFriendshipRepository;
import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.common.exception.UnauthorizedServiceException;
import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockProvider;
import com.mapwithplan.mapplan.friendship.domain.Friendship;
import com.mapwithplan.mapplan.friendship.service.port.FriendshipRepository;
import com.mapwithplan.mapplan.jwt.util.JwtTokenizer;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.service.port.MemberRepository;
import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.service.port.PlanRepository;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@Builder
@Service
@RequiredArgsConstructor
public class PlanShareFriendshipServiceImpl implements PlanShareFriendshipService {

    private final PlanShareFriendshipRepository planShareFriendshipRepository;
    private final PlanRepository planRepository;

    private final FriendshipRepository friendshipRepository;
    private final TimeClockProvider timeClockProvider;
    private final MemberRepository memberRepository;

    private final JwtTokenizer jwtTokenizer;


    /**
     * 공유할 일정, 친구 ID를 기반으로 PlanShareFriendship 만들어 공유된 일정을 저장합니다.
     * @param planId 공유할 일정을 ID 입니다.
     * @param planShareFriendshipIdsList friendshipsIds 를 담은 리스트 입니다.
     * @return
     */
    @Override
    @Transactional
    public List<PlanShareFriendship> sharePlan(Long planId, PlanShareFriendshipList planShareFriendshipIdsList ,String authorizationHeader){
        Plan plan = planRepository.findById(planId);
        Member member = getEmailAndFindMember(authorizationHeader);
        if (plan.getAuthor().getId() != member.getId()){
            throw new UnauthorizedServiceException("접근 불가능한 서버스 입니다.");
        }

        List<Long> friendshipsIds = planShareFriendshipIdsList.getFriendshipsIds();

        List<PlanShareFriendship> PlanShareFriendshipList = getPlanShareFriendships(friendshipsIds, plan);

        return  planShareFriendshipRepository.saveAllSharePlan(PlanShareFriendshipList);
    }

    /**
     * sharePlan 메서드에서 사용할 메서드 입니다. 친구 ID 를 일정에 담아 List<PlanShareFriendship> 를 생성합니다.
     * @param friendshipsIds 친구 ID 리스트 입니다.
     * @param plan 공유할 일정 입니다.
     * @return List<PlanShareFriendship> 공유될 일정이 담긴 리스트 입니다.
     */
    private List<PlanShareFriendship> getPlanShareFriendships(List<Long> friendshipsIds, Plan plan) {
        List<Friendship> findFriendships = friendshipRepository.findAllByIds(friendshipsIds);
        List<PlanShareFriendship> PlanShareFriendshipList = new ArrayList<>();

        for (Friendship findFriendship : findFriendships) {
            PlanShareFriendshipCreate planShareFriendshipCreate = new PlanShareFriendshipCreate(findFriendship, plan);
            PlanShareFriendship planShareFriendship = PlanShareFriendship.from(planShareFriendshipCreate, timeClockProvider);
            PlanShareFriendshipList.add(planShareFriendship);
        }
        return PlanShareFriendshipList;
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
