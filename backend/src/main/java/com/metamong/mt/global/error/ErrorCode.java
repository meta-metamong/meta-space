package com.metamong.mt.global.error;

public enum ErrorCode {
    USER_NOT_FOUND("1001", "아이디가 존재하지 않습니다."),
    PASSWORD_NOT_MATCH("1002", "비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN("1003", "유효하지 않은 토큰입니다."),
    USER_ALREADY_EXISTS("1004", "이미 존재하는 사용자입니다."),
    SERVER_ERROR("5000", "서버 오류가 발생했습니다."),
    LOGOUT_FAILED("1005", "로그아웃 처리 중 오류 발생");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
