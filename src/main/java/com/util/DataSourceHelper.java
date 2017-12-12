package com.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

@Aspect
@Order(0)
public class DataSourceHelper {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceHelper.class);


    public DataSourceHelper() {

    }

    @Pointcut("execution(* com.service.*.*(..))")
    public void pointCut() {

    }

    @Pointcut("@annotation(com.util.annotation.DataSource)")
    public void dataSourcePointCut() {

    }

    @AfterReturning("pointCut()")
    public void afterReturning() throws Throwable {
        DataSourceContextHolder.clearDataSourceType();
    }

    /**
     * 有注解的则切换数据源
     */
    @Before("dataSourcePointCut()")
    public void before(JoinPoint jp) throws Throwable {
        DataSourceContextHolder.setDataSourceType(Constants.SQLSERVER);
    }

    @AfterThrowing(pointcut = "pointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        String targetName = joinPoint.getTarget().getClass().getName();
        String signature = joinPoint.getSignature().getName();
        String errorMsg = e.getMessage();
        logger.info("执行类:[{}],执行方法:[{}],错误信息:[{}]", new Object[]{targetName, signature, errorMsg});
    }
}