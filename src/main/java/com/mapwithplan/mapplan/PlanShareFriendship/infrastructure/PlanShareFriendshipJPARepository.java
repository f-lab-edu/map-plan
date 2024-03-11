package com.mapwithplan.mapplan.PlanShareFriendship.infrastructure;

import com.mapwithplan.mapplan.PlanShareFriendship.infrastructure.entity.PlanShareFriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanShareFriendshipJPARepository extends JpaRepository<PlanShareFriendshipEntity,Long> {
}
