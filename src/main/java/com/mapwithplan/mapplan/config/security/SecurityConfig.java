package com.mapwithplan.mapplan.config.security;



import com.mapwithplan.mapplan.jwt.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
/**
 * JWT 에 필요한 필터를 주입 받으며 password 를 암호와 처리하는 bCryptPasswordEncoder 도 주입한다.
 * @EnableMethodSecurity를 추가하여 메소드 시큐리티를 활성화한다.
 */

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig  {


    // JwtAuthenticationFilter 주입
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    //여기서 AuthenticationEntryPoint는 빈으로 등록된 엔트리포인트를 주입받는다.
    private final AuthenticationEntryPoint entryPoint;
    //BCryptPasswordEncoder 등록
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http.csrf((auth) -> auth.disable())
                //From 로그인 방식 disable
                .formLogin((auth)-> auth.disable())
                //http basic 인증 방식 disable
                .httpBasic((auth) ->auth.disable());


        //경로별 인가 작업
        http.authorizeHttpRequests((auth) ->auth
                // requestMatchers의 인자로 전달된 url은 모두에게 허용
                        .requestMatchers("/login", "/", "/member/create","/member/*/verify").permitAll()
                .anyRequest().authenticated());// 그 외의 모든 요청은 인증 필요

        //jwtAuthenticationFilter 등록
        http.addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                //이 경우에는 앞서 작성했던 JwtAuthenticationEntryPoint를 주입받게 된다.
                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint));



        //세션 설정
        http.sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                })));

        return http.build();
    }

}
