package com.mapwithplan.mapplan.PlanShareFriendship.infrastructure;


import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendship;
import com.mapwithplan.mapplan.PlanShareFriendship.infrastructure.entity.PlanShareFriendshipEntity;
import com.mapwithplan.mapplan.PlanShareFriendship.service.port.PlanShareFriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PlanShareFriendshipRepositoryImpl implements PlanShareFriendshipRepository {

    private final PlanShareFriendshipJPARepository planShareFriendshipJPARepository;


    /**
     * PlanShareFriendship 이 담긴 리스트를 한번에 저장합니다.
     * @param planShareFriendships PlanShareFriendship 이 담긴 리스트 객체 입니다.
     * @return
     */
    @Override
    public List<PlanShareFriendship> saveAllSharePlan(List<PlanShareFriendship> planShareFriendships){
        List<PlanShareFriendshipEntity> list = planShareFriendships
                .stream()
                .map(PlanShareFriendshipEntity::from)
                .toList();
        return planShareFriendshipJPARepository.saveAll(list).stream().map(PlanShareFriendshipEntity::toModel).toList();
    }
}
