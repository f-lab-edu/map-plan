package com.mapwithplan.mapplan.config.security;



import com.mapwithplan.mapplan.jwt.exception.CustomAuthenticationEntryPoint;
import com.mapwithplan.mapplan.jwt.fileter.JwtAuthenticationFilter;
import com.mapwithplan.mapplan.jwt.provider.JwtAuthenticationProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

/**
 * JWT 에 필요한 필터를 주입 받으며 password 를 암호와 처리하는 bCryptPasswordEncoder 도 주입한다.
 * @EnableMethodSecurity를 추가하여 메소드 시큐리티를 활성화한다.
 */

@RequiredArgsConstructor
@Configuration
public class SecurityConfig  {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    //BCryptPasswordEncoder 등록
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http.csrf((auth) -> auth.disable())// CSRF는 Cross Site Request Forgery의 약자. CSRF공격을 막기 위한 방법.
                //From 로그인 방식 disable
                .formLogin((auth)-> auth.disable())
                //http basic 인증 방식 disable
                .httpBasic((auth) ->auth.disable());

        http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration)),
                UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(jwtAuthenticationProvider);
        //경로별 인가 작업
        http.authorizeHttpRequests((auth) ->auth
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                // requestMatchers의 인자로 전달된 url은 모두에게 허용
                        .requestMatchers("/member/login", "/", "/member/create","/member/*/verify").permitAll()
                .anyRequest()
                .authenticated());// 그 외의 모든 요청은 인증 필요


        http.exceptionHandling(handler-> handler.authenticationEntryPoint(customAuthenticationEntryPoint));


        //세션 설정
        http.sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }
    // <<Advanced>> Security Cors로 변경 시도
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
         config.setAllowCredentials(true); // 이거 빼면 된다
        // https://gareen.tistory.com/66
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.setAllowedMethods(List.of("GET","POST","DELETE","PATCH","OPTION","PUT"));
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
