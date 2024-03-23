package com.mapwithplan.mapplan.search.friend.infrastructure;


import com.mapwithplan.mapplan.friendship.infrastructure.entity.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchFriendJpaRepository extends JpaRepository<FriendshipEntity,Long> {

    List<FriendshipEntity> findByFriendNicknameStartingWith(String fiendNickName);
}
