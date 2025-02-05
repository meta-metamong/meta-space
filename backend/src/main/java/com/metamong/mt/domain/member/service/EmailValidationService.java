package com.metamong.mt.domain.member.service;

public interface EmailValidationService {

    /**
     * 주어진 이메일로 랜덤 생성한 코드를 전송
     * @param email 코드를 전송할 이메일
     */
    void sendEmailValidationCode(String email);
    
    /**
     * emailValidationCode가 유효한 코드인지 확인한다.
     * 
     * @param email 유효한 이메일인지 체크할 이메일
     * @param emailValidationCode 확인할 이메일 인증 코드
     * @return 유효한 코드일 경우 true를 반환하고, 유효하지 않을 경우 false를 반환. 유효한
     *         코드일 경우 emailValidationCode를 유효하지 않은 코드로 만든다
     */
    boolean isValidCode(String email, String emailValidationCode);
}
