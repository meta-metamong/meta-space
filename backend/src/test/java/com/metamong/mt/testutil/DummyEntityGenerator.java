package com.metamong.mt.testutil;

import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.model.Role;

/**
 * 테스트를 위한 더미 엔티티 객체 생성 helper 클래스
 */
public class DummyEntityGenerator {

    public static Member generateMember(String userId) {
        return Member.builder()
                .userId(userId)
                .name("김더미")
                .password("asdf1234")
                .role(Role.ROLE_USER)
                .build();
    }
    
    public static Member generateMember() {
        return generateMember("sample1234");
    }
}
