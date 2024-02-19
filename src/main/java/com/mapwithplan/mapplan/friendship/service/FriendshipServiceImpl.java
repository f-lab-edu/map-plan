package com.mapwithplan.mapplan.friendship.service;


import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockProvider;
import com.mapwithplan.mapplan.friendship.controller.port.FriendshipService;
import com.mapwithplan.mapplan.friendship.domain.Friendship;
import com.mapwithplan.mapplan.friendship.domain.FriendshipCreate;
import com.mapwithplan.mapplan.friendship.service.port.FriendshipRepository;
import com.mapwithplan.mapplan.jwt.util.JwtTokenizer;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.service.port.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Builder
@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final TimeClockProvider clockHolder;
    private final JwtTokenizer jwtTokenizer;
    private final MemberRepository memberRepository;

    /**
     * 친구 요청이 이루어지게 되면 요청한 회원, 친구를 찾은 후  Friendship 관계를 생성합니다.
     * @param friendshipCreate 친구 요청에 필요한 DTO 입니다. 요청한 친구의 friendMemberId 를 가지고 있습니다.
     * @param authorizationHeader
     * @return
     */
    @Transactional
    @Override
    public Friendship applyFriendship(FriendshipCreate friendshipCreate, String authorizationHeader) {

        Member member = getEmailAndFindMember(authorizationHeader);
        Member friend = memberRepository.findById(friendshipCreate.getFriendMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Friend", friendshipCreate.getFriendMemberId()));
        Friendship friendship = Friendship.from(friend, member, clockHolder);
        return friendshipRepository.createFriendship(friendship);

    }

    /**
     * 친구 요청 받은 회원이 승인을 하게 됩니다. 친구 관계가 PENDING 에서 ACTIVE 로 변경 됩니다.
     * @param friendshipId 친구 요청의 ID 입니다.
     * @return Friendship 객체를 return 합니다.
     */
    @Transactional
    @Override
    public Friendship approveFriendship(Long friendshipId) {

        return friendshipRepository
                .approveFriendship(friendshipId)
                .orElseThrow(() -> new ResourceNotFoundException("Friend", friendshipId));
    }

    /**
     * 헤더 토큰을 활용해 이메일을 찾아내는 메서드 입니다.
     * @param authorizationHeader 헤더에서 받은 토큰 값입니다.
     * @return 토큰 값을 통해 찾은 회원을 return 합니다.
     */
    private Member getEmailAndFindMember(String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        Claims claims = jwtTokenizer.parseAccessToken(jwtToken);
        String email = claims.getSubject();
        return memberRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Member", email));
    }
}
