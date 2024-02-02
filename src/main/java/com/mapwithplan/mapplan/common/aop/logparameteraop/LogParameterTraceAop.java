package com.mapwithplan.mapplan.common.aop.logparameteraop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;

@Slf4j
@Aspect
public class LogParameterTraceAop {
    /**
     * @Around 애노테이션의 값인 annotation(com.mapwithplan.mapplan..annotation.LogInputTrace)) 는 포인트컷이 된다.
     * @Around 애노테이션의 메서드인 checkInputLog 는 어드바이스( Advice )가 된다.
     * 해당 메서드에 대한 Input 파라미터의 값을 출력한다.
     * 다만 해당
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.mapwithplan.mapplan.common.aop.logparameteraop.annotation.LogInputTrace)")
    public Object checkInputLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Arrays.stream(args)
                .forEach(parameter ->
                        log.info("className = {}, methodName ={}, Parameter: {}",
                                className, methodName, parameter));
        return joinPoint.proceed();
    }
}
