package com.mapwithplan.mapplan.friendship.infrastructure;


import com.mapwithplan.mapplan.common.aop.logparameteraop.annotation.LogInputTrace;
import com.mapwithplan.mapplan.friendship.domain.Friendship;
import com.mapwithplan.mapplan.friendship.domain.FriendshipCreate;
import com.mapwithplan.mapplan.friendship.infrastructure.entity.FriendshipEntity;
import com.mapwithplan.mapplan.friendship.service.port.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 친구 관계에서 사용될 메서드를 구현한 클래스 입니다.
 * FriendshipRepository 와 관련 있습니다.
 */
@Repository
@RequiredArgsConstructor
public class FriendshipRepositoryImpl implements FriendshipRepository {


    private final FriendshipJPARepository friendshipJPARepository;

    /**
     * 생성된 Friendship 친구 관계를 저장합니다.
     * @param friendship 생성된 Friendship 친구 관계 저장에 사용됩니다.
     * @return 저장된 Friendship 을 return 합니다.
     */
    @Override
    public Friendship createFriendship(Friendship friendship) {
        return friendshipJPARepository.save(FriendshipEntity.from(friendship)).toModel();
    }

    /**
     * friendshipId 를 이용해 friendshipEntity 를 찾습니다.
     * @param friendshipId 친구 관계 ID 입니다.
     * @return friendshipId 를 통해 찾은 Optional<Friendship> 을 리턴합니다.
     */
    @Override
    public Optional<Friendship> findById(Long friendshipId) {
        return friendshipJPARepository.findById(friendshipId).map(FriendshipEntity::toModel);
    }

    /**
     * 친구 관계 승인에 필요한 메서드 입니다.
     * friendshipEntity.approve() 를 사용해 EFriendStatus.ACTIVE 로 변경합니다.
     * @param friendshipId 친구 관계 ID 입니다.
     * @return
     */
    @LogInputTrace
    @Override
    public Optional<Friendship> approveFriendship(Long friendshipId) {

        return friendshipJPARepository.findById(friendshipId)
                .map(friendshipEntity -> {
                    friendshipEntity.approve();
                return friendshipEntity;
                })
                .map(FriendshipEntity::toModel);
    }

    /**
     * FriendshipId 리스트를 이용해 한번에 여러 개의 Friendship 객체를 찾아옵니다.
     * @param friendshipIds 입력받은 Ids 리스트입니다.
     * @return FriendshipEntity 를 toModel 로 Friendship 으로 변경하려 return 합니다.
     */
    @Override
    public List<Friendship> findAllByIds(List<Long> friendshipIds) {
        return  friendshipJPARepository
                .findAllById(friendshipIds)
                .stream()
                .map(FriendshipEntity::toModel)
                .toList();
    }
}
