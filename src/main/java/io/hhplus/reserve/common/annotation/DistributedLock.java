package io.hhplus.reserve.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    // 락 이름
    String key();

    // 락 획득 시간
    long waitTime() default 60L;

    // 락 점유 시간
    long leaseTime() default 5000L;

}