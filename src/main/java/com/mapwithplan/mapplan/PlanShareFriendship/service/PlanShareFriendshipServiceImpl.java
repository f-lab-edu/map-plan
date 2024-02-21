package com.mapwithplan.mapplan.PlanShareFriendship.service;


import com.mapwithplan.mapplan.PlanShareFriendship.controller.port.PlanShareFriendshipService;
import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendship;
import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendshipCreate;
import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendshipList;
import com.mapwithplan.mapplan.PlanShareFriendship.service.port.PlanShareFriendshipRepository;
import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
import com.mapwithplan.mapplan.friendship.domain.Friendship;
import com.mapwithplan.mapplan.friendship.service.port.FriendshipRepository;
import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.service.port.PlanRepository;
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
    private final TimeClockHolder timeClockHolder;


    /**
     * 공유할 일정, 친구 ID를 기반으로 PlanShareFriendship 만들어 공유된 일정을 저장합니다.
     * @param planId 공유할 일정을 ID 입니다.
     * @param planShareFriendshipIdsList friendshipsIds 를 담은 리스트 입니다.
     * @return
     */
    @Override
    @Transactional
    public List<PlanShareFriendship> sharePlan(Long planId, PlanShareFriendshipList planShareFriendshipIdsList){
        Plan plan = planRepository.findById(planId);

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
            PlanShareFriendship planShareFriendship = PlanShareFriendship.from(planShareFriendshipCreate, timeClockHolder);
            PlanShareFriendshipList.add(planShareFriendship);
        }
        return PlanShareFriendshipList;
    }


}
