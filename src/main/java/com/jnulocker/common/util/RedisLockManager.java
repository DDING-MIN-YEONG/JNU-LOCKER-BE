package com.jnulocker.common.util;

import com.jnulocker.common.exception.LockAcquisitionFailedException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisLockManager {

    @Value("${custom.lock.default-wait-second:5}")
    private final Long defaultWaitSecond;

    private final RedissonClient redissonClient;
    private final TransactionForSupplier transactionForSupplier;
    private final TransactionForRunnable transactionForRunnable;

    // 반환값이 있는 경우: Supplier<T> 사용
    public <T> T lock(String key, Long waitSecond, Supplier<T> supplier) {
        RLock lock = redissonClient.getLock(key);
        try {
            checkLockable(key, waitSecond, lock);
            return transactionForSupplier.executeWithTransaction(supplier);
        } catch (InterruptedException e) {
            log.error("락 획득 중 인터럽트 발생: {}", key, e);
            Thread.currentThread().interrupt();
            throw LockAcquisitionFailedException.EXCEPTION;
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public void lock(String key, Runnable runnable) {
        lock(key, defaultWaitSecond, runnable);
    }

    // 반환값이 없는 경우: Runnable 사용
    public void lock(String key, Long waitSecond, Runnable runnable) {
        RLock lock = redissonClient.getLock(key);
        try {
            checkLockable(key, waitSecond, lock);
            transactionForRunnable.executeWithTransaction(runnable);
        } catch (InterruptedException e) {
            log.error("락 획득 중 인터럽트 발생: {}", key, e);
            Thread.currentThread().interrupt();
            throw LockAcquisitionFailedException.EXCEPTION;
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private void checkLockable(String key, Long waitSecond, RLock lock)
            throws InterruptedException {
        // Watchdog 방식: leaseTime = -1로 설정하여 자동 갱신 활성화
        boolean lockable = lock.tryLock(waitSecond, -1, TimeUnit.SECONDS);
        if (!lockable) {
            log.error("락 획득 실패: {}", key);
            throw LockAcquisitionFailedException.EXCEPTION;
        }
    }
}
