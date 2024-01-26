package com.mapwithplan.mapplan.login.service;

import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import com.mapwithplan.mapplan.login.domain.CustomUserDetails;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.service.port.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {

        this.memberRepository = memberRepository;
    }
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("CustomUserDetailsService start email = {}", email);
        //DB에서 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Member", email));

        return new CustomUserDetails(member);
    }
}
