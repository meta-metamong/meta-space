package com.metamong.mt.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.metamong.mt.domain.member.dto.request.LoginRequestDto;
import com.metamong.mt.domain.member.dto.request.OwnerSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.UserSignUpRequestDto;
import com.metamong.mt.domain.member.dto.response.LoginInfoResponseDto;
import com.metamong.mt.domain.member.exception.InvalidLoginRequestException;
import com.metamong.mt.domain.member.exception.InvalidLoginRequestType;
import com.metamong.mt.domain.member.exception.PasswordNotConfirmedException;
import com.metamong.mt.domain.member.repository.jpa.MemberRepository;
import com.metamong.mt.domain.member.repository.mybatis.MemberMapper;
import com.metamong.mt.global.mail.MailAgent;
import com.metamong.mt.testutil.DummyEntityGenerator;

@ExtendWith(MockitoExtension.class)
class DefaultMemberServiceTest {
    static final PasswordEncoder pe = NoOpPasswordEncoder.getInstance();
    static final String EXISTS_MEMBER_USER_ID = "exists";
    static final String EXISTS_MEMBER_EMAIL = "exists@gmail.com";
    static final String EXISTS_MEMBER_PASSWORD = pe.encode("1q2w3e4r");

    @InjectMocks
    DefaultMemberService memberService;
    
    @Mock
    MemberMapper memberMapper;
    
    @Mock
    MemberRepository memberRepository;
    
    @Mock
    MailAgent mailAgent;
    
    @Spy
    PasswordEncoder passwordEncoder = pe;
    
    @BeforeEach
    void initPerTest() {
        
        
//        doAnswer(new Answer<>() {
//            
//            @Override
//            public Boolean answer(InvocationOnMock invocation) throws Throwable {
//                String userId = invocation.getArgument(0, String.class);
//                String email = invocation.getArgument(1, String.class);
//                
//                return EXISTS_MEMBER_USER_ID.equals(userId) && EXISTS_MEMBER_EMAIL.equals(email);
//            }
//        }).when(this.memberRepository).existsByUserIdAndEmail(anyString(), anyString());
//        
//        doAnswer(new Answer<>() {
//            
//            @Override
//            public Boolean answer(InvocationOnMock invocation) throws Throwable {
//                String userId = invocation.getArgument(0, String.class);
//                String email = invocation.getArgument(1, String.class);
//                
//                return EXISTS_MEMBER_USER_ID.equals(userId) || EXISTS_MEMBER_EMAIL.equals(email);
//            }
//        }).when(this.memberRepository).existsByUserIdOrEmail(anyString(), anyString());
//        
//        doAnswer(new Answer<>() {
//            
//            @Override
//            public Optional<Member> answer(InvocationOnMock invocation) throws Throwable {
//                String userId = invocation.getArgument(0, String.class);
//                
//                if (EXISTS_MEMBER_USER_ID.equals(userId)) {
//                    return Optional.of(
//                            Member.builder()
//                                    .userId(userId)
//                                    .name("name")
//                                    .password(EXISTS_MEMBER_PASSWORD)
//                                    .phone("010-1111-1111")
//                                    .email(EXISTS_MEMBER_EMAIL)
//                                    .birth(LocalDate.of(1997, 9, 16))
//                                    .postalCode("02222")
//                                    .detailAddress("detailAddress")
//                                    .address("address")
//                                    .role(Role.ROLE_USER)
//                                    .refreshToken("refreshToken")
//                                    .businessName("businessName")
//                                    .businessRegistrationNumber("businessRegistrationNumber")
//                                    .build()
//                    );
//                }
//                return Optional.empty();
//            }
//        }).when(this.memberRepository).findById(anyString());
//        
//        doAnswer(new Answer<>() {
//            
//            @Override
//            public Optional<Member> answer(InvocationOnMock invocation) throws Throwable {
//                String email = invocation.getArgument(0, String.class);
//                
//                if (EXISTS_MEMBER_EMAIL.equals(email)) {
//                    return Optional.of(
//                            Member.builder()
//                                    .userId(EXISTS_MEMBER_USER_ID)
//                                    .name("name")
//                                    .password(EXISTS_MEMBER_PASSWORD)
//                                    .phone("010-1111-1111")
//                                    .email(email)
//                                    .birth(LocalDate.of(1997, 9, 16))
//                                    .postalCode("02222")
//                                    .detailAddress("detailAddress")
//                                    .address("address")
//                                    .role(Role.ROLE_USER)
//                                    .refreshToken("refreshToken")
//                                    .businessName("businessName")
//                                    .businessRegistrationNumber("businessRegistrationNumber")
//                                    .build()
//                    );
//                }
//                return Optional.empty();
//            }
//        }).when(this.memberRepository).findByEmail(anyString());
    }
    
    @Test
    @DisplayName("findLoginInfo() - find success")
    void findLoginInfo_findSuccess() throws Exception {
        // Given
        when(this.memberRepository.findById(EXISTS_MEMBER_USER_ID))
                .thenReturn(Optional.of(
                        DummyEntityGenerator.generateMember(EXISTS_MEMBER_USER_ID)
                ));
        
        Constructor<LoginRequestDto> con = LoginRequestDto.class.getDeclaredConstructor();
        con.setAccessible(true);
        LoginRequestDto requestDto = con.newInstance();
        con.setAccessible(false);
        ReflectionTestUtils.setField(requestDto, "userId", EXISTS_MEMBER_USER_ID);
        ReflectionTestUtils.setField(requestDto, "password", EXISTS_MEMBER_PASSWORD);
        
        // When
        LoginInfoResponseDto result = this.memberService.findLoginInfo(requestDto);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(EXISTS_MEMBER_USER_ID);
    }
    
    @Test
    @DisplayName("findLoginInfo() - member not exists")
    void findLoginInfo_memberNotExists() throws Exception {
        when(this.memberRepository.findById(anyString()))
                .thenReturn(Optional.empty());
        
        Constructor<LoginRequestDto> con = LoginRequestDto.class.getDeclaredConstructor();
        con.setAccessible(true);
        LoginRequestDto requestDto = con.newInstance();
        con.setAccessible(false);
        ReflectionTestUtils.setField(requestDto, "userId", EXISTS_MEMBER_USER_ID + "no");
        ReflectionTestUtils.setField(requestDto, "password", EXISTS_MEMBER_PASSWORD);
        
        // Then
        assertThatExceptionOfType(InvalidLoginRequestException.class).isThrownBy(() -> this.memberService.findLoginInfo(requestDto));
        InvalidLoginRequestException invalidLoginRequestException = catchThrowableOfType(InvalidLoginRequestException.class, () -> this.memberService.findLoginInfo(requestDto));
        
        InvalidLoginRequestType invalidLoginRequestType = invalidLoginRequestException.getInvalidLoginRequestType();
        assertThat(invalidLoginRequestType).isEqualTo(InvalidLoginRequestType.MEMBER_NOT_EXISTS);
    }
    
    @Test
    @DisplayName("findLoginInfo() - incorrect password")
    void findLoginInfo_incorrectPassword() throws Exception {
        when(this.memberRepository.findById(EXISTS_MEMBER_USER_ID))
                .thenReturn(Optional.of(
                        DummyEntityGenerator.generateMember(EXISTS_MEMBER_USER_ID)
                ));
        
        Constructor<LoginRequestDto> con = LoginRequestDto.class.getDeclaredConstructor();
        con.setAccessible(true);
        LoginRequestDto requestDto = con.newInstance();
        con.setAccessible(false);
        ReflectionTestUtils.setField(requestDto, "userId", EXISTS_MEMBER_USER_ID);
        ReflectionTestUtils.setField(requestDto, "password", EXISTS_MEMBER_PASSWORD + "no");
        
        // Then
        assertThatExceptionOfType(InvalidLoginRequestException.class).isThrownBy(() -> this.memberService.findLoginInfo(requestDto));
        InvalidLoginRequestException invalidLoginRequestException = catchThrowableOfType(InvalidLoginRequestException.class, () -> this.memberService.findLoginInfo(requestDto));
        
        InvalidLoginRequestType invalidLoginRequestType = invalidLoginRequestException.getInvalidLoginRequestType();
        assertThat(invalidLoginRequestType).isEqualTo(InvalidLoginRequestType.PASSWORD_INCORRECT);
    }
    
    @Test
    @DisplayName("saveUser() - success to save user")
    void saveUser_successToSaveUser() throws Exception {
        // Given
        when(this.memberRepository.existsByUserIdOrEmail(anyString(), anyString()))
                .thenReturn(false);
        UserSignUpRequestDto arg = new UserSignUpRequestDto();
        ReflectionTestUtils.setField(arg, "userId", "user");
        ReflectionTestUtils.setField(arg, "name", "name");
        ReflectionTestUtils.setField(arg, "password", "1q2w3e4r");
        ReflectionTestUtils.setField(arg, "confirmPassword", "1q2w3e4r");
        ReflectionTestUtils.setField(arg, "email", "user@gmail.com");
        
        // Then
        assertThatNoException()
                .isThrownBy(() -> this.memberService.saveUser(arg));
    }
    
    @Test
    @DisplayName("saveUser() - passwor not confirmed")
    void saveUser_passwordNotConfirmed() {
        // Given
        UserSignUpRequestDto arg = new UserSignUpRequestDto();
        ReflectionTestUtils.setField(arg, "userId", "user");
        ReflectionTestUtils.setField(arg, "name", "name");
        ReflectionTestUtils.setField(arg, "password", "1q2w3e4r");
        ReflectionTestUtils.setField(arg, "confirmPassword", "anotherpassword");
        
        // Then
        assertThatExceptionOfType(PasswordNotConfirmedException.class)
                .isThrownBy(() -> this.memberService.saveUser(arg));
    }
    
    @Test
    @DisplayName("saveOwner() - success to save")
    void saveOwner_successToSave() {
        // Given
        when(this.memberRepository.existsByUserIdOrEmail(anyString(), anyString()))
                .thenReturn(false);
        OwnerSignUpRequestDto arg = new OwnerSignUpRequestDto();
        ReflectionTestUtils.setField(arg, "userId", "user");
        ReflectionTestUtils.setField(arg, "name", "name");
        ReflectionTestUtils.setField(arg, "password", "1q2w3e4r");
        ReflectionTestUtils.setField(arg, "confirmPassword", "1q2w3e4r");
        ReflectionTestUtils.setField(arg, "email", "user@gmail.com");
        
        // Then
        assertThatNoException()
                .isThrownBy(() -> this.memberService.saveOwner(arg));
    }
}
