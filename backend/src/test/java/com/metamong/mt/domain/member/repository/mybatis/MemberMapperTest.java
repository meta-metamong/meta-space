package com.metamong.mt.domain.member.repository.mybatis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.metamong.mt.domain.member.dto.response.LoginInfoResponseDto;
import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.model.constant.Role;
import com.metamong.mt.domain.member.repository.jpa.MemberRepository;
import com.metamong.mt.domain.member.repository.mybatis.MemberMapper;
import com.metamong.mt.testutil.DummyEntityGenerator;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class MemberMapperTest {
    
    @Autowired
    MemberMapper memberMapper;
    
    @Autowired
    MemberRepository memberRepository;
//
//    @Test
//    @DisplayName("findLoginInfoByUserId() - 성공")
//    void findLoginInfoByUserId_successs() {
//        // Given
//        Member dummy = DummyEntityGenerator.generateMember();
//        String dummyId = dummy.getUserId();
//        String dummyName = dummy.getName();
//        Role dummyRole = dummy.getRole();
//        this.memberRepository.save(dummy);
//        
//        // When
//        LoginInfoResponseDto result = this.memberMapper.findLoginInfoByUserId(dummyId).get();
//        
//        // Then
//        assertThat(result.getUserId()).isEqualTo(dummyId);
//        assertThat(result.getName()).isEqualTo(dummyName);
//        assertThat(result.getRole()).isEqualTo(dummyRole);
//    }
}
