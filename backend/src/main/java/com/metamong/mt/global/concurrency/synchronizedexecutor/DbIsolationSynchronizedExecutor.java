package com.metamong.mt.global.concurrency.synchronizedexecutor;

import java.util.function.Supplier;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.global.concurrency.SynchronizedExecutor;

@Component
public class DbIsolationSynchronizedExecutor implements SynchronizedExecutor {

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public Object executeWithLock(Supplier<Object> targetLogic) throws Throwable {
        return targetLogic.get();
    }
}
