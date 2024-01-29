package com.mapwithplan.mapplan.common.exception;

/**
 * JWT의 공통된 예외를 처리합니다.
 */
public class JwtCommonException extends RuntimeException{

    public JwtCommonException() {
    }

    public JwtCommonException(String message) {
        super(message);
    }


}
