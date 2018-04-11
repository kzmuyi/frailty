package com.yangyongqiang.frailty.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Y_YQ
 * @DATE 11/4/18
 * @DESC
 */
@Data
public class FrailtyResponse implements Serializable {
    private Integer code;
    private String msg;
    private Object data;

    private FrailtyResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private FrailtyResponse(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private enum Status {
        OK(200, "Success."), PARAM_ERROR(300, "Param error."),
        BIZ_ERROR(400, "Business error."), SYSTEM_ERROR(500, "System error."),
        NO_LOGIN(700, "No login error.");

        int code;
        String msg;

        Status(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    public static FrailtyResponse successResponse() {
        return new FrailtyResponse(Status.OK.code, Status.OK.msg);
    }

    public static FrailtyResponse successResponse(String msg) {
        return new FrailtyResponse(Status.OK.code, msg);
    }

    public static FrailtyResponse successResponseWithData(Object obj) {
        return new FrailtyResponse(Status.OK.code, Status.OK.msg, obj);
    }

    public static FrailtyResponse paramErrorResponse() {
        return new FrailtyResponse(Status.PARAM_ERROR.code, Status.PARAM_ERROR.msg);
    }

    public static FrailtyResponse paramErrorResponse(String msg) {
        return new FrailtyResponse(Status.PARAM_ERROR.code, msg);
    }

    public static FrailtyResponse bizErrorResponse() {
        return new FrailtyResponse(Status.BIZ_ERROR.code, Status.BIZ_ERROR.msg);
    }

    public static FrailtyResponse bizErrorResponse(String msg) {
        return new FrailtyResponse(Status.BIZ_ERROR.code, msg);
    }

    public static FrailtyResponse sysErrorResponse() {
        return new FrailtyResponse(Status.SYSTEM_ERROR.code, Status.SYSTEM_ERROR.msg);
    }

    public static FrailtyResponse sysErrorResponse(String msg) {
        return new FrailtyResponse(Status.SYSTEM_ERROR.code, msg);
    }

    public static FrailtyResponse noLoginResponse() {
        return new FrailtyResponse(Status.NO_LOGIN.code, Status.NO_LOGIN.msg);
    }

}
