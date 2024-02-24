package com.mapwithplan.mapplan.friendship.infrastructure;

import com.mapwithplan.mapplan.friendship.infrastructure.entity.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipJPARepository extends JpaRepository<FriendshipEntity,Long> {



}
