package com.mapwithplan.mapplan.friendship.service.port;

import com.mapwithplan.mapplan.friendship.domain.Friendship;

import java.util.List;
import java.util.Optional;

/**
 * 현재 FriendshipRepositoryImpl 에서 implements 를 받아 구현합니다.
 */
public interface FriendshipRepository {

    /**
     * 친구 관계를 생성하는 메서드입니다.
     * @param friendship
     * @return  생성된 친구 관계 friendship 을 return 합니다.
     */
    Friendship createFriendship(Friendship friendship);

    /**
     * 친구 관계를 ID 를 통해 조회하는 메서드 입니다.
     * @param friendshipId 친구 관계 ID 입니다.
     * @return
     */
    Optional<Friendship> findById(Long friendshipId);

    /**
     *  친구 요청을 통해 승인 되었을 때, Friendship EFriendStatus 가 ACTIVE 로 변경 되어야합니다.
     *  이 메서드를 통해 구현을 진행하시면 됩니다.
     * @param friendshipId 친구 관계 ID 입니다.
     * @return 승인된 친구 관계를 return 합니다.
     */
    Optional<Friendship> approveFriendship(Long friendshipId);

    /**
     * FriendshipId 리스트를 이용해 한번에 여러 개의 Friendship 객체를 찾아옵니다.
     * @param friendshipIds 친구 관계  id 를 담은 리스트 입니다.
     * @return 찾은 친구를 return 합니다.
     */
    List<Friendship> findAllByIds(List<Long> friendshipIds);


}
