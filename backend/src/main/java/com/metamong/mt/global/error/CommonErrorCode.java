package com.metamong.mt.global.error;

import com.metamong.mt.global.apispec.ErrorCodeSpecifiable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public enum CommonErrorCode implements ErrorCodeSpecifiable {
    SERVER_ERROR(500, "SERVER001", "서버 오류가 발생했습니다.");

    private final int statusCode;
    private final String errorCode;
    private final String message;
}
