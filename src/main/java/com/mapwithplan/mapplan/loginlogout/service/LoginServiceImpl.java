package com.mapwithplan.mapplan.loginlogout.service;


import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
import com.mapwithplan.mapplan.jwt.util.JwtTokenizer;
import com.mapwithplan.mapplan.loginlogout.controller.port.LoginService;
import com.mapwithplan.mapplan.loginlogout.controller.response.LoginResponse;
import com.mapwithplan.mapplan.loginlogout.domain.Login;
import com.mapwithplan.mapplan.loginlogout.domain.RefreshToken;
import com.mapwithplan.mapplan.loginlogout.service.port.RefreshTokenRepository;
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
@Builder
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TimeClockHolder timeClockHolder;

    @Override
    @Transactional
    public LoginResponse login(Login login){
        Member member = memberRepository.findByEmail(login.getEmail())
                .filter(findMember -> encoder.matches(login.getPassword(), findMember.getPassword()))
                .orElseThrow(() -> new ResourceNotFoundException("member", login.getEmail()));
        if(member.getMemberStatus() == EMemberStatus.INACTIVE || member.getMemberStatus() ==EMemberStatus.PENDING){
            throw new IllegalArgumentException("접근 불가능한 계정입니다.");
        }

        String memberRoles = member.getEMemberRole().toString();
        List<String> Roles  = new ArrayList<>();
        Roles.add(memberRoles);

        //토큰 생성
        String accessToken = jwtTokenizer.createAccessToken(member.getId(), member.getEmail(), Roles,timeClockHolder);
        String refreshToken = jwtTokenizer.createRefreshToken(member.getId(), member.getEmail(), Roles,timeClockHolder);

        Optional<RefreshToken> findToken = refreshTokenRepository.findByMember(member);
        if (findToken.isPresent()){
            findToken.get().update(refreshToken);
            refreshTokenRepository.update(findToken.get());
        } else {
            refreshTokenRepository
                    .save(RefreshToken.from(member, refreshToken));
        }

        return LoginResponse.from(member,accessToken,refreshToken);
    }

    @Override
    public void logout(String refreshToken) {
        log.info("refreshToken = {}", refreshToken);
        refreshTokenRepository.delete(refreshToken);
    }
}
