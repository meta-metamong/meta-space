package com.metamong.mt.domain.member.controller;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
        LoginRequestDto loginDto = new LoginRequestDto("prov@naver.com", "123456");
        
        //When & Then
        Assertions.assertThrows(InvalidLoginRequestException.class, () -> memberService.login(loginDto));
    }
    
    @Test
    @DisplayName("기본 요소 테스트")
    void testSimple() {
        // Mock
        List<String> mockList = Mockito.mock(List.class);
        
        // Stub
        Mockito.when(mockList.size()).thenReturn(5);
        System.out.println("동작 값을 확인합니다. [" + mockList.size() + "]");
        
        // Verify
        Mockito.verify(mockList).size();
    }
}
