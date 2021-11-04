package com.example.demo.cache;

import java.util.concurrent.TimeUnit;

public interface RedisDistributeLock {


    /**
     * @param lockName
     * @param request
     * @param leaseTime
     * @param unit
     * @param retryTimes
     * @return
     */
    boolean tryLock(String lockName, String request, long leaseTime, TimeUnit unit, int retryTimes);

    /**
     * @param lockName
     * @param request
     * @param leaseTime
     * @param unit
     * @return
     */
    boolean lock(String lockName, String request, long leaseTime, TimeUnit unit);

    /**
     * @param lockName
     * @param request
     */
    void unlock(String lockName, String request);

}
