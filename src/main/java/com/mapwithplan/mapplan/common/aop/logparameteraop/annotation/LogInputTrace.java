package com.mapwithplan.mapplan.common.aop.logparameteraop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 각 메서드에 대한 Input 파라미터 값을 출력하기 위한 어노테이션 입니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogInputTrace {
}
