package com.mapwithplan.mapplan.search.friend.service;

import com.mapwithplan.mapplan.search.friend.controller.port.SearchFriendService;
import com.mapwithplan.mapplan.search.friend.service.port.SearchFriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class SearchFriendServiceImpl implements SearchFriendService {




    private final SearchFriendRepository searchFriendRepository;
}
