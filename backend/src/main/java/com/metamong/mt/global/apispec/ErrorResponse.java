package com.metamong.mt.global.apispec;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>공통 에러 응답. 에러 상황에서 이 객체를 반환해야 한다.
 * <p>코드 예시:<br>
 * - MemberErrorCode
 * <pre>
 * {@code
 * @RequiredArgsConstructor(access = AccessLevel.PACKAGE) 
 * @Getter
 * public enum MemeberErrorCode {
 *     MEMBER_NOT_FOUND(404, "MEM001", "해당 회원이 존재하지 않습니다.");
 *     
 *     private final int statusCode;
 *     private final String errorCode;
 *     private final String message;
 * }
 * </pre>
 * - ExceptionHandler
 * <pre>
 * {@code
 * @ExceptionHandler(MemberNotFoundException.class)
 * public ResponseEntity<ErrorResponse> memberNotFoundException(MemberNotFoundException e) {
 *     Map<String, Object> detail = Map.of("memberId", e.getMemberId());
 *     return ErrorResponse.of(MemberErrorCode.MEMBER_NOT_FOUND, detail);
 * }
 * </pre>
 * 
 * - 응답 예시 - JSON
 * <pre>
 * {@code
 * {
 *     "errorName": "MEMEBER_NOT_FOUND",
 *     "statusCode": 404,
 *     "errorCode": "MEM001",
 *     "message": "해당 회원이 존재하지 않습니다.",
 *     "detail": {
 *         "memberId": 13
 *     }
 * }
 * </pre>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class ErrorResponse {
    private final String errorName;
    private final int statusCode;
    private final String errorCode;
    private final String message;
    private final Object detail;
    
    public static ErrorResponse of(ErrorCodeSpecifiable errorCodeSpecifiable, Object detail) {
        return ErrorResponse.builder()
                .errorName(errorCodeSpecifiable.name())
                .statusCode(errorCodeSpecifiable.getStatusCode())
                .errorCode(errorCodeSpecifiable.getErrorCode())
                .message(errorCodeSpecifiable.getMessage())
                .detail(detail)
                .build();
    }
    
    public static ErrorResponse of(ErrorCodeSpecifiable errorCodeSpecifiable) {
        return of(errorCodeSpecifiable, null);
    }
}
