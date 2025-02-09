package com.metamong.mt.domain.payment.exception.errorcode;

import com.metamong.mt.global.apispec.ErrorCodeSpecifiable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public enum PaymentErrorCode implements ErrorCodeSpecifiable{
    PAY_NOT_FOUND(404, "PAY001", "결제 정보가 존재하지 않습니다."),
    NOT_ENOUGH_MONEY(409, "PAY002", "판매자 계좌 잔금이 환불 금액보다 적습니다."),
    ACCOUNT_NOT_FOUND(404, "PAY003", "판매자 계좌가 조회되지 않습니다.");
    
    private final int statusCode;
    private final String errorCode;
    private final String message;
}
