package com.yangyongqiang.frailty.common.exception;

import java.io.Serializable;

/**
 * @Author KaiJia
 * @DATE 2016-04-15
 * @DESC 自定义异常
 */
public abstract class FrailtyCustomException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -2360740272511651214L;

    public FrailtyCustomException() {
        super();
    }

    public FrailtyCustomException(String message) {
        super(message);
    }

    public FrailtyCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
