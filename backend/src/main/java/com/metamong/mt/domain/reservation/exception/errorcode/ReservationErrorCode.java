package com.metamong.mt.domain.reservation.exception.errorcode;

import com.metamong.mt.global.apispec.ErrorCodeSpecifiable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public enum ReservationErrorCode implements ErrorCodeSpecifiable {
    RVT_NOT_FOUND(404, "RVT001", "예약 정보가 존재하지 않습니다."),
	DUPLICATED_RESERVATION(409, "RVT002", "예약 인원 수가 초과되었습니다."),
    INVALID_REQUEST(400, "RVT003", "입력 값이 올바르지 않습니다.");
    
    private final int statusCode;
    private final String errorCode;
    private final String message;
}
