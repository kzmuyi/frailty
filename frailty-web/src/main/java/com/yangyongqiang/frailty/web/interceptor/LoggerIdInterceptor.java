package com.yangyongqiang.frailty.web.interceptor;

/**
 * @Author Y_YQ
 * @DATE 10/4/18
 * @DESC
 */

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * 设置日志Id,  可在任务入口方法上加入此advice, 作为任务id, 方便追踪任务
 */
public class LoggerIdInterceptor implements MethodInterceptor {
    private static final String LOGGER_ID_NAME = "logger_id";

    private void setLoggerId() {
        MDC.put(LOGGER_ID_NAME, DigestUtils.md5Hex(UUID.randomUUID().toString()));
    }

    private void unsetLoggerId() {
        MDC.remove(LOGGER_ID_NAME);
    }

    private boolean loggerIdSetted() {
        return StringUtils.isNotBlank(MDC.get(LOGGER_ID_NAME));
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        boolean needUpdateLoggerId = !loggerIdSetted();

        if (needUpdateLoggerId) {
            setLoggerId();
        }
        try {
            return invocation.proceed();
        } finally {
            if (needUpdateLoggerId) {
                unsetLoggerId();
            }
        }
    }
}
