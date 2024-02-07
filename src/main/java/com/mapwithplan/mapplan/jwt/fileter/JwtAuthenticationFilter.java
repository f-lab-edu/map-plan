package com.mapwithplan.mapplan.jwt.fileter;

import com.mapwithplan.mapplan.jwt.provider.JwtAuthenticationProvider;
import com.mapwithplan.mapplan.jwt.token.JwtAuthenticationToken;
import com.mapwithplan.mapplan.jwt.exception.JwtExceptionCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter 는 요청으로 들어온 것을 토큰에 대한 값을 검증하는 필터 입니다.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager ;

    /**
     *  요청으로 들어온 필터를 분류 후 검증한 다음 예외를 처리하는 필터입니다.
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("request {}", request.getRequestURI());
        String token="";
        try {
            token = getToken(request);
            if (StringUtils.hasText(token)) {
                getAuthentication(token);
            }
            filterChain.doFilter(request, response);
        }
        catch (NullPointerException | IllegalStateException e) {
            request.setAttribute("exception", JwtExceptionCode.NOT_FOUND_TOKEN.getCode());
            log.error("Not found Token // token : {}", token);
            log.error("Set Request Exception Code : {}", request.getAttribute("exception"));
            throw new BadCredentialsException("throw new not found token exception");
        } catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", JwtExceptionCode.INVALID_TOKEN.getCode());
            log.error("Invalid Token // token : {}", token);
            log.error("Set Request Exception Code : {}", request.getAttribute("exception"));
            throw new BadCredentialsException("throw new invalid token exception");
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", JwtExceptionCode.EXPIRED_TOKEN.getCode());
            log.error("EXPIRED Token // token : {}", token);
            log.error("Set Request Exception Code : {}", request.getAttribute("exception"));
            throw new BadCredentialsException("throw new expired token exception");
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", JwtExceptionCode.UNSUPPORTED_TOKEN.getCode());
            log.error("Unsupported Token // token : {}", token);
            log.error("Set Request Exception Code : {}", request.getAttribute("exception"));
            throw new BadCredentialsException("throw new unsupported token exception");
        } catch (Exception e) {
            log.error("====================================================");
            log.error("JwtFilter - doFilterInternal() 오류 발생");
            log.error("token : {}", token);
            log.error("Exception Message : {}", e.getMessage());
            log.error("Exception StackTrace : {");
            e.printStackTrace();
            log.error("}");
            log.error("====================================================");
            throw new BadCredentialsException("throw new exception");
        }
    }

    /**
     * 토큰이 없는 경우 예외를 발생 시키며 권한도 분류합니다.
     * @param token
     */
    private void getAuthentication(String token) {
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext()
                .setAuthentication(authenticate);
    }

    /**
     * 토큰 값을 getHeader 통해 받은후 Bearer와 분리합니다.
     * 그후 토큰값을 받습니다.
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer")){
            String[] arr = authorization.split(" ");
            return arr[1];
        }
        return null;
    }
}