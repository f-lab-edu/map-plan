package com.mapwithplan.mapplan.common.exception.controller;


import com.mapwithplan.mapplan.common.exception.CertificationCodeNotMatchedException;
import com.mapwithplan.mapplan.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 *  이 클래스에서는 공통으로 적용할 수 있는 예외를 담는 클래스입니다.
 *  - DB 에서 조회 불가능한 데이터
 *  - 인증이 일치하지 않을때
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    @ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public String resourceNotFoundException(ResourceNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(CertificationCodeNotMatchedException.class)
    public String certificationCodeNotMatchedException(CertificationCodeNotMatchedException exception) {
        return exception.getMessage();
    }

}
