package com.example.webtest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerfAspect {

    @Around("bean(simpleEventService)")   
    public Object logPerf(ProceedingJoinPoint pip) throws Throwable {
        Long begin = System.currentTimeMillis();
        Object ret = pip.proceed();
        System.out.println(System.currentTimeMillis() - begin);
        return ret;
    }
}
