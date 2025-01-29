package com.metamong.mt.domain.member.exception.errorcode;

import com.metamong.mt.global.apispec.ErrorCodeSpecifiable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public enum MemberErrorCode implements ErrorCodeSpecifiable {
    MEMBER_NOT_FOUND(404, "MEM001", "회원 정보가 존재하지 않습니다."),
    INVALID_LOGIN_REQUEST(401, "MEM002", "해당 정보로 로그인할 수 없습니다."),
    PASSWORD_NOT_CONFIRMED(400, "MEM003", "비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD_RESET_REQUEST(400, "MEM004", "회원의 아이디와 이메일이 일치하지 않습니다."),
	ID_EMAIL_ALEADY_EXIST(400, "MEM005", "이미 존재하는 ID 또는 이메일입니다.");
    
    private final int statusCode;
    private final String errorCode;
    private final String message;
}
