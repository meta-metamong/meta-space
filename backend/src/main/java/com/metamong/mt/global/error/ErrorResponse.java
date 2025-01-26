package com.metamong.mt.global.error;

public class ErrorResponse {
    private String code;
    private String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
