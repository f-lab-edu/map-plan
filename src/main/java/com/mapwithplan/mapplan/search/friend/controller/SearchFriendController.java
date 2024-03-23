package com.mapwithplan.mapplan.search.friend.controller;


import com.mapwithplan.mapplan.search.friend.controller.port.SearchFriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class SearchFriendController {

    private final SearchFriendService searchFriendService;
}
