package com.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DataSourceHelper {
    public DataSourceHelper() {
    }

    @Pointcut("execution(* com.dao.*.*(..))")
    public void DataSourcePoint() {

    }

    @Before("DataSourcePoint()")
    public void beforeCallMethod(JoinPoint pjp) throws ClassNotFoundException {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        String className = targetMethod.getDeclaringClass().getName();
        //可以配置n个数据库，dao命名必须带下面的字母
        if (className.contains("SqlServer")) {
            DynamicDataSource.setDataSourceKey(DynamicDataSourceGlobal.SQLSERVER);
        } else {
            DynamicDataSource.setDataSourceKey(DynamicDataSourceGlobal.MYSQL);
        }
    }
}