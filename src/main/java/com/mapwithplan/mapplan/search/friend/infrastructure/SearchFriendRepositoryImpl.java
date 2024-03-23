package com.mapwithplan.mapplan.search.friend.infrastructure;


import com.mapwithplan.mapplan.search.friend.service.port.SearchFriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class SearchFriendRepositoryImpl implements SearchFriendRepository {

    private final SearchFriendJpaRepository searchFriendJpaRepository;


}
