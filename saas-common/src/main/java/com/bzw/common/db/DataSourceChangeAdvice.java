package com.bzw.common.db;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

/**
 * @author yanbin
 */
@Aspect
public class DataSourceChangeAdvice {

    private DataSourceSwitcher switcher;

    public DataSourceChangeAdvice(DataSourceSwitcher switcher) {
        this.switcher = switcher;
    }


    @Before("execution(* com.bzw.api.module.*.Service.*(..))")
    public void setWriteDataSourceType(JoinPoint point) {
        Service service = point.getTarget().getClass().getAnnotation(Service.class);
        if (service==null) {
            return;
        }
        SlaveSource slaveSource =  ((MethodSignature)point.getSignature()).getMethod().getAnnotation(SlaveSource.class);
        if (slaveSource==null) {
            switcher.setMaster();
        }
        else {
            switcher.setSlave();
        }
    }
}
