package com.metamong.mt.domain.member.repository.redis;

public interface MemberVolatileCodeRepository {

    void saveEmailValidationCode(String email, String code);
    
    String findEmailValidationCodeByEmail(String email);
    
    boolean deleteByEmail(String email);
}
