package com.mapwithplan.mapplan.loginlogout.service;


import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockProvider;
import com.mapwithplan.mapplan.jwt.util.JwtTokenizer;
import com.mapwithplan.mapplan.loginlogout.controller.port.LoginService;
import com.mapwithplan.mapplan.loginlogout.controller.response.LoginResponse;
import com.mapwithplan.mapplan.loginlogout.domain.Login;
import com.mapwithplan.mapplan.loginlogout.domain.RefreshToken;
import com.mapwithplan.mapplan.member.domain.EMemberStatus;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.service.port.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 로그인, 로그아웃을 다루는 Service 입니다.
 * LoginService 인터페이스를 구현했습니다.
 */
@Builder
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenizer jwtTokenizer;
    private final TimeClockProvider timeClockProvider;
    private final RefreshTokenService refreshTokenService;

    /**
     * 회원에 대한 login 를 주입 받은후 조회합니다. 조회 불가능시 ResourceNotFoundException 을 호출합니다.
     * 회원의 INACTIVE PENDING 상태를 검증합니다.
     * 회원의 역할 MEMBER 또는 ADMIN 을 리스트에 저장하고 토큰을 생성시 사용합니다.
     * jwtTokenizer 을 통해 accessToken, refreshToken 생성하고 refreshToken 만료시 재접속이면 업데이트를 실행하고
     * 만료후 다시로그인 시 새로 저장을 합니다.
     * @param login email, password 를 담고 있습니다.
     * @return
     */
    @Override
    @Transactional
    public LoginResponse login(Login login){
        Member member = memberRepository.findByEmail(login.getEmail())
                .filter(findMember -> encoder.matches(login.getPassword(), findMember.getPassword()))
                .orElseThrow(() -> new ResourceNotFoundException("member", login.getEmail()));

        memberStatusVerification(member.getMemberStatus());

        String memberRoles = member.getEMemberRole().toString();
        List<String> Roles  = new ArrayList<>();
        Roles.add(memberRoles);

        //토큰 생성
        String accessToken = jwtTokenizer.createAccessToken(member.getId(), member.getEmail(), Roles, timeClockProvider);
        String refreshToken = jwtTokenizer.createRefreshToken(member.getId(), member.getEmail(), Roles, timeClockProvider);

        saveOrUpdateRefreshToken(member,refreshToken);

        return LoginResponse.from(member,accessToken,refreshToken);
    }

    /**
     * 로그아웃 시도 시 DB 에 refreshToken 정보를 삭제 요청합니다.
     * @param refreshToken
     */
    @Override
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.deleteToken(refreshToken);
    }

    /**
     * 회원의 상태를 검증하는 내부 메서드 입니다.
     * @param eMemberStatus
     */
    private void memberStatusVerification(EMemberStatus eMemberStatus){
        if(eMemberStatus == EMemberStatus.INACTIVE || eMemberStatus ==EMemberStatus.PENDING){
            throw new IllegalArgumentException("접근 불가능한 계정입니다.");
        }
    }

    /**
     * 토근에 대한 저장 업데이트를 진행합니다.
     * @param member
     * @param refreshToken
     */
    private void saveOrUpdateRefreshToken(Member member, String refreshToken){

        Optional<RefreshToken> findToken = refreshTokenService.findByMember(member);
        if (findToken.isPresent()){
            findToken.get().update(refreshToken);
            refreshTokenService.updateRefreshToken(findToken.get());
        } else {
            refreshTokenService.saveRefreshToken(RefreshToken.from(member, refreshToken));
        }
    }
}
