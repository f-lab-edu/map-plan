package com.mapwithplan.mapplan.friendship.infrastructure;


import com.mapwithplan.mapplan.friendship.service.port.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendshipRepositoryImpl implements FriendshipRepository {


    private final FriendshipJPARepository friendshipJPARepository;

}
