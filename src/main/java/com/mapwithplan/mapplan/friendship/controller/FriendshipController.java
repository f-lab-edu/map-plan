package com.mapwithplan.mapplan.friendship.controller;


import com.mapwithplan.mapplan.common.aop.logparameteraop.annotation.LogInputTrace;
import com.mapwithplan.mapplan.friendship.controller.port.FriendshipService;
import com.mapwithplan.mapplan.friendship.controller.response.FriendshipApplyResponse;
import com.mapwithplan.mapplan.friendship.controller.response.FriendshipApproveResponse;
import com.mapwithplan.mapplan.friendship.domain.Friendship;
import com.mapwithplan.mapplan.friendship.domain.FriendshipCreate;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Builder
@RestController
@RequestMapping("/friendship")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    /**
     * 친구 요청을 보내는 컨트롤러 입니다.
     * @param authorizationHeader 토큰 헤더에 있는 값을 활용해 신청 회원의 정보를 얻습니다.
     * @param friendshipCreate 친구의 회원 ID를 가지고 있는 DTO 입니다. 친구 요청에 사용됩니다.
     * @return 친구 요청 ID 와
     */
    @LogInputTrace
    @PostMapping("/apply")
    public ResponseEntity<FriendshipApplyResponse> applyFriendship(@RequestHeader("Authorization") String authorizationHeader,
                                                                   @RequestBody FriendshipCreate friendshipCreate) {

        Friendship friendship = friendshipService.applyFriendship(friendshipCreate, authorizationHeader);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FriendshipApplyResponse.from(friendship));
    }

    /**
     * 친구 요청을 받은 친구가 승인을 하는 컨트롤러 입니다.
     * @param friendshipId 요청 받은 친구요청 ID
     * @return 승인한 friendName (친구 이름) 을 리턴 합니다.
     */
    @LogInputTrace
    @PostMapping("/approve/{friendshipId}")
    public ResponseEntity<FriendshipApproveResponse> approveFriendship(@RequestHeader("Authorization") String authorizationHeader,
                                                                       @PathVariable("friendshipId") Long friendshipId){
        Friendship friendship = friendshipService.approveFriendship(authorizationHeader, friendshipId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new FriendshipApproveResponse(friendship.getFriendMemberId().getName()));
    }

}
