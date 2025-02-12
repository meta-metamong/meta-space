package com.metamong.mt.domain.report.exception.errorcode;

import com.metamong.mt.global.apispec.ErrorCodeSpecifiable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public enum ReportErrorCode implements ErrorCodeSpecifiable{
    REPORT_NOT_FOUND(404, "RPT001", "결제 정보가 존재하지 않습니다.");
    
    private final int statusCode;
    private final String errorCode;
    private final String message;
}
