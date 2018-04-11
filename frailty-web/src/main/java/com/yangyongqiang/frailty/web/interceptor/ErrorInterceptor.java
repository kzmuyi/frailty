package com.yangyongqiang.frailty.web.interceptor;

import com.google.gson.Gson;
import com.yangyongqiang.frailty.common.FrailtyResponse;
import com.yangyongqiang.frailty.common.exception.FrailtyCustomException;
import com.yangyongqiang.frailty.common.exception.FrailtyParamException;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @Author Y_YQ
 * @DATE 10/4/18
 * @DESC
 */
@Slf4j
public class ErrorInterceptor implements MethodInterceptor {
    private static final Gson GSON = new Gson();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } catch (FrailtyParamException e) {
            return FrailtyResponse.paramErrorResponse(e.getMessage());
        } catch (FrailtyCustomException e) {
            return FrailtyResponse.bizErrorResponse(e.getMessage());
        } catch (Exception e) {
            log.error("method {}.{} with param {} got an error {}",
                    invocation.getThis().getClass().getSimpleName(), invocation.getMethod().getName(),
                    GSON.toJson(invocation.getArguments()),
                    ExceptionUtils.getStackTrace(e));
            return FrailtyResponse.sysErrorResponse();
        }
    }
}