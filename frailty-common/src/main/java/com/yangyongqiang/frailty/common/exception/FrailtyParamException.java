package com.yangyongqiang.frailty.common.exception;

import java.io.Serializable;

/**
 * @Author KaiJia
 * @DATE 2016-04-08
 * @DESC 参数异常
 */
public class FrailtyParamException extends FrailtyCustomException implements Serializable {

    private static final long serialVersionUID = 595236877321910104L;

    public FrailtyParamException() {
        super();
    }

    public FrailtyParamException(String message) {
        super(message);
    }

    public FrailtyParamException(String message, Throwable cause) {
        super(message, cause);
    }
}
