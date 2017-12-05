package com.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Order(0)
public class DataSourceHelper {
    public DataSourceHelper() {

    }

    @AfterReturning("execution(* com.service.*.*(..))")
    public void afterReturning() throws Throwable {
        DataSourceContextHolder.clearDataSourceType();
    }

    /**
     * 有注解的则切换数据源
     */
    @Before("execution(* com.service.*.*(..)) && @annotation(com.util.annotation.DataSource)")
    public void before(JoinPoint jp) throws Throwable {
        DataSourceContextHolder.setDataSourceType(Constants.SQLSERVER);
    }
}