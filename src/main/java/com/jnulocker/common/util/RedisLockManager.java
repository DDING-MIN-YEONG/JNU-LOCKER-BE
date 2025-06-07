package com.jnulocker.common.util;

import com.jnulocker.common.exception.LockAcquisitionFailedException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
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

    @Value("${custom.lock.lease-time}")
    private Long leaseTime;

    private final RedissonClient redissonClient;
    private final TransactionForSupplier transactionForSupplier;
    private final TransactionForConsumer transactionForConsumer;

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

    // 반환값이 없는 경우: Consumer<Void> 사용
    public void lock(String key, Long waitSecond, Consumer<Void> consumer) {
        RLock lock = redissonClient.getLock(key);
        try {
            checkLockable(key, waitSecond, lock);
            transactionForConsumer.executeWithTransaction(consumer);
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
        boolean lockable = lock.tryLock(waitSecond, leaseTime, TimeUnit.SECONDS);
        if (!lockable) {
            log.error("락 획득 실패: {}", key);
            throw LockAcquisitionFailedException.EXCEPTION;
        }
    }
}
