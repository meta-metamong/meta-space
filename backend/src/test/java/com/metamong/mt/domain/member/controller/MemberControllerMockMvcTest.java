package com.metamong.mt.domain.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import com.metamong.mt.domain.member.dto.response.LoginInfoResponseDto;
import com.metamong.mt.domain.member.model.constant.Role;
import com.metamong.mt.domain.member.service.MemberService;
import com.metamong.mt.global.jwt.JwtTokenProvider;
import com.metamong.mt.global.web.cookie.CookieGenerator;
import com.metamong.mt.global.web.cookie.DefaultCookieGenerator;

@WebMvcTest(controllers = MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(MemberControllerMockMvcTest.SpyConfig.class)
class MemberControllerMockMvcTest {
    static final String EXISTS_USER_ID = "exists";
    static final String MOCK_ACCESS_TOKEN = "accesstoken";
    static final String MOCK_REFRESH_TOKEN = "refreshtoken";
    
    @Autowired
    MockMvc mockMvc; // 테스트용 요청을 보내는 객체
    
    // MemberController에 주입되는 Mock 객체들
    @MockitoBean
    MemberService memberService;
    
    @MockitoBean
    JwtTokenProvider jwtTokenProvider;
    
    // MemberController에 주입되는 실제 객체
    @MockitoSpyBean
    CookieGenerator cookieGenerator;
    
    @TestConfiguration
    static class SpyConfig {
        
        @Bean
        public CookieGenerator cookieGenerator() {
            return new DefaultCookieGenerator("localhost");
        }
    }

    @Test
    @DisplayName("POST /api/members/login - 로그인 성공")
    void post_login_successtoLogin() throws Exception {
        // Given
        when(this.memberService.findLoginInfo(any()))
                .thenReturn(LoginInfoResponseDto.builder()
                        .userId(EXISTS_USER_ID)
                        .name("name")
                        .role(Role.ROLE_USER)
                        .build());
        
        when(this.jwtTokenProvider.generateAccessToken(any()))
                .thenReturn(MOCK_ACCESS_TOKEN);
        when(this.jwtTokenProvider.generateRefreshToken(any()))
                .thenReturn(MOCK_REFRESH_TOKEN);
        
        doNothing().when(this.memberService)
                .updateRefreshToken(any(), any());
        
        // When
        this.mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(String.format("""
                                {
                                    "userId": "%s",
                                    "password": "1q2w3e4r"
                                }
                                """, EXISTS_USER_ID)))
                .andDo(print()).andDo(log())
                .andExpect(status().isOk())
                .andExpect(cookie().value("Refresh-Token", MOCK_REFRESH_TOKEN))
                .andExpect(header().stringValues("X-Access-Token", MOCK_ACCESS_TOKEN));
    }
}
