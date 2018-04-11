package com.yangyongqiang.frailty.common.exception;

import java.io.Serializable;

/**
 * @Author KaiJia
 * @DATE 2016-04-08
 * @DESC 业务异常
 */
public class FrailtyBizException extends FrailtyCustomException implements Serializable {

    private static final long serialVersionUID = 8942510104110275584L;

    public FrailtyBizException() {
    }

    public FrailtyBizException(String message) {
        super(message);
    }
}
