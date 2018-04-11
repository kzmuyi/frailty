package com.yangyongqiang.frailty.common.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author Y_YQ
 * @DATE 10/4/18
 * @DESC
 */
public class RetryUtil {
    private static final Logger log = LoggerFactory.getLogger(RetryUtil.class);

    public interface RetryCmd<V> {
        V execute() throws Throwable;
    }

    /**
     * 重试策略
     */
    public interface RetryPolicy {
        boolean allowRetry(int retryCount, long elapsedTimeMs);
    }

    /**
     * 一直重试，直到成功
     */
    public static class RetryForever implements RetryPolicy {
        private final int retryIntervalMs;

        public RetryForever(int retryIntervalMs) {
            this.retryIntervalMs = retryIntervalMs;
        }

        @Override
        public boolean allowRetry(int retryCount, long elapsedTimeMs) {
            try {
                TimeUnit.MILLISECONDS.sleep(retryIntervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            return true;
        }
    }

    private abstract static class SleepingRetry implements RetryPolicy {
        private final int n;

        protected SleepingRetry(int n) {
            this.n = n;
        }

        public boolean allowRetry(int retryCount, long elapsedTimeMs) {
            if (retryCount < n) {
                try {
                    TimeUnit.MILLISECONDS.sleep(getSleepTimeMs(retryCount, elapsedTimeMs));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
                return true;
            }
            return false;
        }

        protected abstract int getSleepTimeMs(int retryCount, long elapsedTimeMs);
    }

    /**
     * 重试直至超时
     */
    public static class RetryUntilElapsed extends SleepingRetry {

        private final int maxElapsedTimeMs;
        private final int sleepMsBetweenRetries;

        public RetryUntilElapsed(int maxElapsedTimeMs, int sleepMsBetweenRetries) {
            super(Integer.MAX_VALUE);
            this.maxElapsedTimeMs = maxElapsedTimeMs;
            this.sleepMsBetweenRetries = sleepMsBetweenRetries;
        }

        @Override
        public boolean allowRetry(int retryCount, long elapsedTimeMs) {
            return super.allowRetry(retryCount, elapsedTimeMs) && (elapsedTimeMs < maxElapsedTimeMs);
        }

        @Override
        protected int getSleepTimeMs(int retryCount, long elapsedTimeMs) {
            return sleepMsBetweenRetries;
        }
    }

    /**
     * 重试N次
     */
    public static class RetryNTimes extends SleepingRetry {

        private final int sleepMsBetweenRetries;

        public RetryNTimes(int n, int sleepMsBetweenRetries) {
            super(n);
            this.sleepMsBetweenRetries = sleepMsBetweenRetries;
        }

        @Override
        protected int getSleepTimeMs(int retryCount, long elapsedTimeMs) {
            return sleepMsBetweenRetries;
        }
    }

    public static class RetryOneTime extends RetryNTimes {
        public RetryOneTime(int sleepMsBeforeRetry) {
            super(1, sleepMsBeforeRetry);
        }
    }

    /**
     * @see #retryOnThrowable(Set, RetryPolicy, RetryCmd)
     */
    public static <V> V retryOnThrowable(Class<? extends Throwable> clazz,
                                         RetryPolicy retryPolicy, RetryCmd<V> cmd) throws Throwable {
        return retryOnThrowable(Collections.singleton(clazz), retryPolicy, cmd);
    }

    /**
     * 抛出异常时重试cmd
     * <p>
     * timeoutms参数和maxRetry参数决定了超时时间和最大重试次数, 这两个参数至少有一个为非负
     *
     * @param classes     指定重试的错误类型
     * @param retryPolicy 重试策略.
     * @param cmd         执行的指令
     * @throws Throwable
     */
    public static <V> V retryOnThrowable(Set<Class<? extends Throwable>> classes,
                                         RetryPolicy retryPolicy, RetryCmd<V> cmd) throws Throwable {

        long startTime = System.currentTimeMillis();
        int retry = 0;

        while (true) {
            try {
                return cmd.execute();
            } catch (Throwable t) {
                if (retryPolicy.allowRetry(retry++, System.currentTimeMillis() - startTime)
                        && isInstanceOf(classes, t)) {
                    log.warn("got error, will retry[{}], {}", retry, ExceptionUtils.getStackTrace(t));
                    continue;
                }
                throw t;
            }
        }
    }

    private static boolean isInstanceOf(Set<Class<? extends Throwable>> classes, Throwable t) {
        for (Class c : classes) {
            if (c.isInstance(t)) {
                return true;
            }
        }
        return false;
    }
}