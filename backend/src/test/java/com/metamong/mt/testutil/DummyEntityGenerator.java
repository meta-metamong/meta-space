package com.metamong.mt.testutil;

import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.model.constant.Role;

/**
 * 테스트를 위한 더미 엔티티 객체 생성 helper 클래스
 */
public class DummyEntityGenerator {
    
    public static Member generateMember(String userId, String name, String password, String email, Role role) {
        return Member.builder()
                .memName(name)
                .password(password)
                .email(email)
                .role(role)
                .build();
    }
    
    public static Member generateMemberNormalUser(String userId, String name, String password, String email) {
        return generateMember(userId, name, password, email, Role.ROLE_CONS);
    }
    
    public static Member generateMember(String userId, String password) {
        return generateMember(userId, "김더미", password, userId + "@gmail.com", Role.ROLE_CONS);
    }

    public static Member generateMember(String userId) {
        return generateMember(userId, "1q2w3e4r");
    }
    
    public static Member generateMember() {
        return generateMember("sample1234");
    }
}
