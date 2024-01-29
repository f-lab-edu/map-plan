package com.mapwithplan.mapplan.common.exception.controller;

import com.mapwithplan.mapplan.common.exception.JwtCommonException;
import io.jsonwebtoken.ExpiredJwtException;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * JWT를 통해 발생하는 예외들을 다루는 컨트롤러입니다.
 */
@RestControllerAdvice
public class ExceptionControllerJWT {

    /**
     * 유효하지 않은 토큰에 대한 예외 입니다.
     * @return JwtCommonException 을 return 합니다.
     */
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<JwtCommonException> handleSignatureException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtCommonException("토큰이 유효하지 않습니다."));
    }

    /**
     * 올바르지 않은 토큰 예외입니다.
     * @return JwtCommonException
     */
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<JwtCommonException> handleMalformedJwtException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtCommonException("올바르지 않은 토큰입니다."));
    }

    /**
     * 만료된 토큰의 예외처리입니다.
     * @return JwtCommonException
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<JwtCommonException> handleExpiredJwtException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtCommonException("토큰이 만료되었습니다. 다시 로그인해주세요."));
    }
}
