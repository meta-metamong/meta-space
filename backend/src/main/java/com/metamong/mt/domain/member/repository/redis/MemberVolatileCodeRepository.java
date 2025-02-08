package com.metamong.mt.domain.member.repository.redis;

/**
 * 회원과 관련된 여러 휘발성 코드들을 저장하고 다루는 Repository
 */
public interface MemberVolatileCodeRepository extends MemberEmailCodeRepository {

    /**
     * 회원가입 시 유효한 회원가입 요청인지 체크하는 유효성 코드를 저장하는 메소드.
     * 
     * @param email 유효한지 체크할 이메일
     * @param code 코드
     */
    void saveSignUpValidationCode(String email, String code);
    
    /**
     * 회원가입 시 유효한 회원가입 요청인지 체크하는 유효성 코드를 반환하는 메소드.
     * 
     * @param email key
     * @return email로 저장된 코드가 있을 경우 코드 반환, 없을 경우 null
     */
    String findSignUpValidationCodeByEmail(String email);
    
    /**
     * 유효성을 체크할 이메일을 key로 저장된 key-value 쌍을 삭제한다.
     * 
     * @param email key
     * @return key에 해당하는 pair가 있어서 삭제에 성공하면 true, 그렇지 않으면 false
     */
    boolean deleteSignUpValidationCodeByEmail(String email);
}
