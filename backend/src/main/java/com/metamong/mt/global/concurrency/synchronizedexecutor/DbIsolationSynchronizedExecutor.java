package com.metamong.mt.global.concurrency.synchronizedexecutor;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.global.concurrency.SynchronizedExecutor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DbIsolationSynchronizedExecutor implements SynchronizedExecutor {
    private final ReentrantLock lock;

    @Override
    public Object executeWithLock(Supplier<Object> targetLogic) throws Throwable {
        try {
            lock.lockInterruptibly();
            return targetLogic.get();
        } finally {
            lock.unlock();
        }
    }
}
