package com.metamong.mt.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.metamong.mt.domain.member.dto.request.ConsumerSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.LoginRequestDto;
import com.metamong.mt.domain.member.dto.response.LoginResponseDto;
import com.metamong.mt.domain.member.exception.EmailAlreadyExistException;
import com.metamong.mt.domain.member.exception.IllegalSignUpRequestException;
import com.metamong.mt.domain.member.exception.InvalidLoginRequestException;
import com.metamong.mt.domain.member.exception.MemberNotFoundException;
import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.model.constant.Gender;
import com.metamong.mt.domain.member.model.constant.Role;
import com.metamong.mt.domain.member.repository.jpa.MemberRepository;
import com.metamong.mt.domain.member.repository.mybatis.MemberMapper;
import com.metamong.mt.global.constant.BooleanAlt;

@ExtendWith(MockitoExtension.class)
class DefaultMemberServiceTest {
    @InjectMocks
    DefaultMemberService memberService;
    
    @Mock
    MemberMapper memberMapper;
    
    @Mock
    MemberRepository memberRepository;
    
    @Mock
    PasswordEncoder passwordEncoder;
    
    @Mock
    EmailValidationService emailValidationService;
    
    static Member testMember;
    
    @BeforeEach
    void setupTestMember() {
        testMember = Member.builder()
                .memId((long)1)
                .email("testkhs@naver.com")
                .memName("김한수")
                .password("1234")
                .memPhone("010-4118-0614")
                .gender(Gender.M)
                .birthDate(LocalDate.of(1999, 6, 14))
                .memPostalCode("24209")
                .memDetailAddress("강원도 춘천시 소양강로 104")
                .memAddress("101-702")
                .isDel(BooleanAlt.N)
                .role(Role.ROLE_CONS)
                .build();
    }
    
    @Test
    @DisplayName("로그인 실패 - 이메일 틀림")
    void notValidEmailLoginTest() {
        // Given
        LoginRequestDto notFoundDto1 = new LoginRequestDto("imnotfound@naver.com", "1234");
        
        // When
        when(this.memberRepository.findByEmail(notFoundDto1.getEmail())).thenThrow(MemberNotFoundException.class);
        
        // Then
        assertThrows(InvalidLoginRequestException.class, () -> {
            this.memberService.login(notFoundDto1);
        });
    }
    
    @Test
    @DisplayName("로그인 실패 - 탈퇴한 회원")
    void deletedMemberLoginTest() {
        // Given
        testMember.setIsDel(BooleanAlt.Y);
        
        // When
        when(this.memberRepository.findByEmail(testMember.getEmail())).thenReturn(Optional.of(testMember));

        // Then
        assertThrows(InvalidLoginRequestException.class, () -> {
            this.memberService.login(new LoginRequestDto(testMember.getEmail(), "1234"));
        });
    }
    
    @Test
    @DisplayName("로그인 실패 - 정지된 회원")
    void bannedMemberLoginTest() {
        // Given
        testMember.setMemBannedUntil(LocalDateTime.now().plusDays(10));
        
        // When
        when(this.memberRepository.findByEmail(testMember.getEmail())).thenReturn(Optional.of(testMember));
        
        //Then
        Exception exception = assertThrows(InvalidLoginRequestException.class, () -> {
            this.memberService.login(new LoginRequestDto(testMember.getEmail(), "1234"));
        });
        
        System.out.println(exception.getMessage());
    }
    
    @Test
    @DisplayName("로그인 실패 - 비밀번호 틀림")
    void invalidPasswordMemberLoginTest() {
        // Given
        LoginRequestDto dto = new LoginRequestDto("testkhs@naver.com", "4321");
        
        // When
        when(this.memberRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(testMember));
        when(this.passwordEncoder.matches(dto.getPassword(), testMember.getPassword())).thenReturn(false);

        // Then
        assertThrows(InvalidLoginRequestException.class, () -> {
            this.memberService.login(dto);
        });
    }
    
    @Test
    @DisplayName("로그인 성공")
    void loginTest() {
        // Given
        LoginRequestDto dto = new LoginRequestDto("testkhs@naver.com", "1234");
        
        // When
        when(this.memberRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(testMember));
        when(this.passwordEncoder.matches(dto.getPassword(), testMember.getPassword())).thenReturn(true);
        
        // Then
        LoginResponseDto response = this.memberService.login(dto);
        assertThat(response != null);
    }
    
    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void duplicatedEmailTest() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // Given
        Constructor<ConsumerSignUpRequestDto> constructor = ConsumerSignUpRequestDto.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        ConsumerSignUpRequestDto dto = constructor.newInstance();
        constructor.setAccessible(false);
        
        ReflectionTestUtils.setField(dto, "email", testMember.getEmail());
        
        // When
        when(this.memberRepository.existsByEmail(dto.getEmail())).thenReturn(true);
        
        // Then
        assertThrows(EmailAlreadyExistException.class, () -> {
            this.memberService.saveConsumer(dto);
        });
    }
    
    @Test
    @DisplayName("회원가입 실패 - 올바르지 않은 인증코드")
    void notValidatedEmailAuthCodeTest() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // Given
        Constructor<ConsumerSignUpRequestDto> constructor = ConsumerSignUpRequestDto.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        ConsumerSignUpRequestDto dto = constructor.newInstance();
        constructor.setAccessible(false);
        
        ReflectionTestUtils.setField(dto, "email", testMember.getEmail());
        ReflectionTestUtils.setField(dto, "signUpValidationCode", "1234");

        // When
        when(this.memberRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(this.emailValidationService.isValidSignUpValidationCode(dto.getEmail(), dto.getSignUpValidationCode())).thenReturn(false);
        
        // Then
        assertThrows(IllegalSignUpRequestException.class, () -> {
            this.memberService.saveConsumer(dto);
        });
    }
    
    @Test
    @DisplayName("회원가입 성공")
    void signupTest() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
        // Given
        Constructor<ConsumerSignUpRequestDto> constructor = ConsumerSignUpRequestDto.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        ConsumerSignUpRequestDto dto = constructor.newInstance();
        constructor.setAccessible(false);
        
        ReflectionTestUtils.setField(dto, "email", testMember.getEmail());
        ReflectionTestUtils.setField(dto, "memName", testMember.getMemName());
        ReflectionTestUtils.setField(dto, "password", testMember.getPassword());
        ReflectionTestUtils.setField(dto, "memPhone", testMember.getMemPhone());
        ReflectionTestUtils.setField(dto, "gender", testMember.getGender().toString());
        ReflectionTestUtils.setField(dto, "birthDate", testMember.getBirthDate());
        ReflectionTestUtils.setField(dto, "memPostalCode", testMember.getMemPostalCode());
        ReflectionTestUtils.setField(dto, "memAddress", testMember.getMemAddress());
        ReflectionTestUtils.setField(dto, "memDetailAddress", testMember.getMemDetailAddress());
        ReflectionTestUtils.setField(dto, "signUpValidationCode", "1234");

        // When
        when(this.memberRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(this.emailValidationService.isValidSignUpValidationCode(dto.getEmail(), dto.getSignUpValidationCode())).thenReturn(true);
        when(this.passwordEncoder.encode(dto.getPassword())).thenReturn("1234");
        
        // Then
        this.memberService.saveConsumer(dto);
        verify(this.memberRepository, times(1)).save(any(Member.class)); 
    }
}
