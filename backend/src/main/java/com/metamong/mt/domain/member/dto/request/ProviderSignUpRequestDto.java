package com.metamong.mt.domain.member.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.metamong.mt.domain.member.dto.request.validation.EnumValidator;
import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.model.constant.Gender;
import com.metamong.mt.domain.member.model.constant.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime birthDate; 

    @NotEmpty(message = "우편번호는 필수입니다.")
    private String memPostalCode;
    
    @NotEmpty(message = "주소는 필수입니다.")
    private String memAddress;

    @NotEmpty(message = "상세 주소는 필수입니다.")
    private String memDetailAddress;
    
    private String businessName;
    private String businessNumber;
    private String bankCode;
    private String account;
    private String accountOwner;
    
    
    public Member toEntity() {
        return Member.builder()
			 .password(this.password)
			 .memName(this.memName)
			 .email(this.email)
			 .memPhone(this.memPhone)
			 .gender(Gender.valueOf(this.gender))
			 .birthDate(this.birthDate)
			 .memPostalCode(this.memPostalCode)
			 .memAddress(this.memAddress)
			 .memDetailAddress(this.memDetailAddress)
		    .role(Role.ROLE_PROV)
		    .build();
    }
}
