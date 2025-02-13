package com.metamong.mt.domain.member.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.metamong.mt.domain.member.dto.request.LoginRequestDto;
import com.metamong.mt.domain.member.exception.InvalidLoginRequestException;
import com.metamong.mt.domain.member.service.MemberService;

@SpringBootTest
public class MemberControllerTests {
    @Autowired
    private MemberService memberService;
    
    @Test
    @DisplayName("로그인 테스트")
    void login() {
        // Given
        LoginRequestDto successDto = new LoginRequestDto("prov@naver.com", "123456");
        LoginRequestDto failDto = new LoginRequestDto("lalala@naver.com", "123456");
        
        //When & Then
//        Assertions.assertTrue("카리나".equals(memberService.login(successDto).getName()));
        Assertions.assertThrows(InvalidLoginRequestException.class, () -> memberService.login(failDto));
    }
}
