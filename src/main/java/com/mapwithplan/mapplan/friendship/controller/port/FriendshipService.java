package com.mapwithplan.mapplan.friendship.controller.port;

import com.mapwithplan.mapplan.friendship.domain.Friendship;
import com.mapwithplan.mapplan.friendship.domain.FriendshipCreate;

/**
 * Port 역할을 하며, Controller 에 사용하는 인터페이스입니다.
 * FriendshipServiceImpl 에서 구현을 진행했습니다.
 */
public interface FriendshipService {

    /**
     * 친구요청에 사용할 메서드를 구현합니다.
     * @param friendshipCreate Friendship 친구 관계 생성에 필요한 DTO 입니다.
     * @param authorizationHeader 헤더에 있는 값을 활용해 접근한 회원 정보를 찾아냅니다.
     * @return 생성된 친구관계 Friendship 을 return 합니다.
     */
    Friendship applyFriendship(FriendshipCreate friendshipCreate, String authorizationHeader);

    /**
     * 생성된 친구 관계에서 EFriendStatus 의 값을 ACTIVE 로 변경합니다.
     * 친구 관계가 PENDING 에서 ACTIVE 로 변경됩니다.
     * @param friendshipId 변경할 friendship ID 입니다.
     * @return
     */
    Friendship approveFriendship(Long friendshipId);
}
