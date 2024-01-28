package com.mapwithplan.mapplan.login.service;


import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.jwt.TokenProvider;
import com.mapwithplan.mapplan.login.controller.response.LoginResponse;
import com.mapwithplan.mapplan.login.domain.Login;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;


    @Transactional(readOnly = true)
    public LoginResponse login(Login login){

        Member member = memberRepository
                .findByEmail(login.getEmail())
                .filter(findMember -> encoder.matches(login.getPassword(),findMember.getPassword()))
                .orElseThrow(() -> new ResourceNotFoundException("Member", login.getEmail()));

        //토큰 생성 로직
        String token = tokenProvider.createToken(String.format("%s:%s", member.getId(), member.getEMemberType()));
        log.info("토큰 = {}",token);
        return LoginResponse.from(member,token);
    }
}
