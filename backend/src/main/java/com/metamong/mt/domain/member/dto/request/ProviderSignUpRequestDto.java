package com.metamong.mt.domain.member.dto.request;

import java.time.LocalDate;

import com.metamong.mt.domain.member.dto.request.validation.EnumValidator;
import com.metamong.mt.domain.member.model.FctProvider;
import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.model.constant.Gender;
import com.metamong.mt.domain.member.model.constant.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProviderSignUpRequestDto {

	@NotEmpty(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
	
    @NotEmpty(message = "이름은 필수입니다.")
    private String memName;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password;

    @NotEmpty(message = "전화번호는 필수입니다.")
    private String memPhone;
    
    @NotBlank(message = "성별은 필수입니다.")
    @EnumValidator(enumClass=Gender.class, message="성별 값은 'M' 또는 'W'만 가능합니다.")
    private String gender;

    @Past(message = "생일은 과거의 날짜만 가능합니다.")
    private LocalDate birthDate; 

    @NotEmpty(message = "우편번호는 필수입니다.")
    private String memPostalCode;
    
    @NotEmpty(message = "주소는 필수입니다.")
    private String memAddress;

    @NotEmpty(message = "상세 주소는 필수입니다.")
    private String memDetailAddress;
    
    @NotEmpty(message = "사업자명은 필수입니다.")
    private String bizName;
    
    @NotEmpty(message = "사업자등록번호는 필수입니다.")
    private String bizRegNum;
    
    private ProviderSignupAccountRequestDto account;
    
    @NotNull
    @NotEmpty
    private String signUpValidationCode;
    
    public Gender getGender() {
        return Gender.valueOf(this.gender);
    }
    
    public Member toEntity() {
        return Member.builder()
			 .password(this.password)
			 .memName(this.memName)
			 .email(this.email)
			 .memPhone(this.memPhone)
			 .gender(this.getGender())
			 .birthDate(this.birthDate)
			 .memPostalCode(this.memPostalCode)
			 .memAddress(this.memAddress)
			 .memDetailAddress(this.memDetailAddress)
             .role(Role.ROLE_PROV)
             .build();
    }
    
    public FctProvider toProvider() {
        return FctProvider.builder()
                .bizName(this.bizName)
                .bizRegNum(this.bizRegNum)
                .build();
    }
}
