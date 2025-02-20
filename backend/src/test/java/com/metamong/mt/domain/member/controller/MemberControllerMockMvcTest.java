package com.metamong.mt.domain.member.controller;

import static org.assertj.core.api.Assertions.offset;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metamong.mt.domain.member.dto.response.LoginResponseDto;
import com.metamong.mt.domain.member.dto.response.MemberResponseDto;
import com.metamong.mt.domain.member.model.constant.Gender;
import com.metamong.mt.domain.member.model.constant.Role;
import com.metamong.mt.domain.member.service.EmailValidationService;
import com.metamong.mt.domain.member.service.MemberService;
import com.metamong.mt.global.auth.jwt.JwtTokenProvider;
import com.metamong.mt.global.config.BeanConfig;
import com.metamong.mt.global.web.cookie.CookieGenerator;

import jakarta.servlet.http.Cookie;

@WebMvcTest(controllers = MemberController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(BeanConfig.class)
class MemberControllerMockMvcTest {
    
    @Autowired
    MockMvc mockMvc; // 테스트용 요청을 보내는 객체
    
    // MemberController에 주입되는 Mock 객체들
    @MockitoBean
    MemberService memberService;
    
    @MockitoBean
    JwtTokenProvider jwtTokenProvider;
    
    @MockitoBean
    UserDetailsService userDetailsService;
    
    // MemberController에 주입되는 실제 객체
    @MockitoSpyBean
    CookieGenerator cookieGenerator;
    
    @MockitoBean
    EmailValidationService emailValidationService;
    
    @Autowired
    ObjectMapper om;

    @Test
    @DisplayName("POST /api/members/login - 로그인 성공")
    void post_login_successtoLogin() throws Exception {
        // Given
        final String EXISTS_MEM_ID = "exists@gmail.com";
        final String MOCK_ACCESS_TOKEN = "accesstoken";
        final String MOCK_REFRESH_TOKEN = "refreshtoken";
        
        when(this.memberService.login(any()))
                .thenReturn(LoginResponseDto.builder()
                        .memId(330L)
                        .name("name")
                        .role(Role.ROLE_PROV)
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
                                    "email": "%s",
                                    "password": "1q2w3e4r"
                                }
                                """, EXISTS_MEM_ID)))
                .andDo(print()).andDo(log())
                .andExpect(status().isOk())
                .andExpect(cookie().value("Refresh-Token", MOCK_REFRESH_TOKEN))
                .andExpect(header().stringValues("X-Access-Token", MOCK_ACCESS_TOKEN));
    }
    
    @Test
    @DisplayName("POST /members/consumer - success")
    void registerUser_success() throws Exception {
        doNothing().when(this.memberService).saveConsumer(any());
        
        this.mockMvc.perform(post("/api/members/consumer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email": "example@gmail.com",
                            "memName": "NAME",
                            "password": "1q2w3e4r",
                            "memPhone": "010-1234-1234",
                            "gender": "M",
                            "birthDate": "1997-09-16",
                            "memPostalCode": "01234",
                            "memAddress": "ADDR",
                            "memDetailAddress": "address",
                            "signUpValidationCode": "135rhi3f134"
                        }
                        """))
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("POST /members/provider - success")
    void registerOwner_success() throws Exception {
        doNothing().when(this.memberService).saveProvider(any());
        
        this.mockMvc.perform(post("/api/members/provider")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email": "example@gmail.com",
                            "memName": "NAME",
                            "password": "1q2w3e4r",
                            "memPhone": "010-1234-1234",
                            "gender": "M",
                            "birthDate": "1997-09-16",
                            "memPostalCode": "01234",
                            "memAddress": "ADDR",
                            "memDetailAddress": "address",
                            "signUpValidationCode": "135rhi3f134",
                            "bizName": "BIZ",
                            "bizRegNum": "13531-512424",
                            "bankCode": "021",
                            "accountNumber": "135113-13513",
                            "isAgreedInfo": "Y"
                        }
                        """))
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("GET /members/reissue")
    void reissue_success() throws Exception {
        final String accessToken = "3151refqadsav";
        final String refreshToken = "eqifoeqh031rt1ffda";
        
        when(this.jwtTokenProvider.resolveToken(any())).thenReturn(accessToken);
        when(this.jwtTokenProvider.resolveRefreshTokenFromCookie(any())).thenReturn(refreshToken);
        when(this.jwtTokenProvider.validateToken(accessToken)).thenReturn(false);
        when(this.jwtTokenProvider.validateToken(refreshToken)).thenReturn(true);
        when(this.jwtTokenProvider.getMemberId(anyString())).thenReturn("330");
        when(this.userDetailsService.loadUserByUsername(anyString())).thenReturn(new UserDetails() {
            
            @Override
            public String getUsername() {
                return "330";
            }
            
            @Override
            public String getPassword() {
                return "135315";
            }
            
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }
        });
        
        doNothing().when(this.memberService).updateRefreshToken(anyLong(), any());
        
        this.mockMvc.perform(get("/api/members/reissue")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .cookie(new Cookie("Refresh-Token", refreshToken)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("Refresh-Token"));
    }
    
    @Test
    @DisplayName("POST /members/send-validation-emails")
    void sendValidationEmail() throws JsonProcessingException, Exception {
        when(this.emailValidationService.sendEmailValidationCode(anyString()))
                .thenReturn("asdfwaefqefqe");
        
        this.mockMvc.perform((post("/api/members/send-validation-emails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.om.writeValueAsBytes(Map.of("email", "example@gmail.com")))))
        .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("POST /members/check-email-validation")
    void checkEmailValidation_success() throws Exception {
        final String validationCode = "feqhfoqehf3rhd80asv";
        when(this.emailValidationService.validateCode(anyString(), anyString()))
                .thenReturn(validationCode);
        
        this.mockMvc.perform(post("/api/members/check-email-validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "example@gmail.com",
                                    "emailValidationCode": "er13qe"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json(String.format("""
                        {
                            "statusCode": 200,
                            "content": "%s"
                        }
                        """, validationCode)));
    }
    
    @Test
    @DisplayName("POST /members/logout - success")
    void logout_success() throws Exception {
        when(this.jwtTokenProvider.resolveRefreshTokenFromCookie(any()))
                .thenReturn("asdfheofqifqd");
        when(this.jwtTokenProvider.getMemberId(anyString())).thenReturn("300");
        doNothing().when(this.memberService).deleteRefreshToken(anyLong());
        
        
        this.mockMvc.perform(post("/api/members/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("Refresh-Token", "asdfheofqifqd"))
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(cookie().maxAge("Refresh-Token", 0))
                .andExpect(content().json("""
                        {
                            "statusCode": 200
                        }
                        """));
    }
    
    @Test
    @DisplayName("GET /api/members/{memberId} - success")
    void getMember_success() throws Exception {
        MemberResponseDto responseDto = MemberResponseDto.builder()
                .memId(300L)
                .email("example@gmail.com")
                .memName("EMA")
                .memPhone("010-1234-1234")
                .gender(Gender.M)
                .birthDate(LocalDate.of(2000, 1, 2))
                .memPostalCode("12345")
                .memAddress("ADDR")
                .memDetailAddress("DADDR")
                .imgPath("http://localhost:8080/resources/files/img_01.png")
                .role(Role.ROLE_CONS)
                .bizName("BIZ")
                .bizRegNum("123456-12356716")
                .bankCode("001")
                .bankName("MYBANK")
                .accountNumber("31531124-24124")
                .balance(60000L)
                .build();
        when(this.memberService.searchMember(anyLong()))
                .thenReturn(responseDto);
        
        Map<String, Object> expectedDtoObj = Map.of(
                "statusCode", 200,
                "content", responseDto
        );
        
        String expectedResponseBody = this.om.writeValueAsString(expectedDtoObj);
        
        this.mockMvc.perform(get("/api/members/{memId}", 300L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }
}
