package io.hhplus.reserve.common.aop;

import io.hhplus.reserve.common.annotation.DistributedLock;
import io.hhplus.reserve.support.domain.exception.BusinessException;
import io.hhplus.reserve.support.domain.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopTransaction aopTransaction;

    @Around("@annotation(io.hhplus.reserve.common.annotation.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        Object[] args = joinPoint.getArgs();
        String key = REDISSON_LOCK_PREFIX + args[0];
        RLock lock = redissonClient.getLock(key);

        try {
            boolean available = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), TimeUnit.MILLISECONDS);
            if (!available) {
                throw new BusinessException(ErrorType.WAITING_KEY_BAD_REQUEST);
            }

            return aopTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            try {
                lock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock already unlocked for service: {}, key: {}", method.getName(), key);
            }
        }
    }
}
