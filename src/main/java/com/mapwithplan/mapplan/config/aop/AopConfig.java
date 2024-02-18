package com.mapwithplan.mapplan.config.aop;


import com.mapwithplan.mapplan.common.aop.logparameteraop.LogParameterTraceAop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AopConfig {

    @Bean
    public LogParameterTraceAop logInputTrace(){
        return new LogParameterTraceAop();
    }
}
