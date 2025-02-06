package com.metamong.mt.domain.member.dto.request;

import java.time.LocalDateTime;

import com.metamong.mt.domain.member.dto.request.validation.EnumValidator;
import com.metamong.mt.domain.member.model.constant.Gender;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateRequestDto {
    @NotEmpty(message = "이름은 필수입니다.")
    private String memName;

    @Size(min = 6, message = "비밀번호는 6자리 이상이어야 합니다.")
    private String password;

    @NotEmpty(message = "전화번호는 필수입니다.")
    private String memPhone;

    @Past(message = "생일은 과거의 날짜만 가능합니다.")
    private LocalDateTime birthDate; 
    
    @NotEmpty(message = "성별은 필수입니다.")
    @EnumValidator(enumClass=Gender.class, message="성별 값은 'M' 또는 'W'만 가능합니다.")
    private String gender;

    @NotEmpty(message = "우편번호는 필수입니다.")
    private String memPostalCode;

    @NotEmpty(message = "상세 주소는 필수입니다.")
    private String memDetailAddress;

    @NotEmpty(message = "주소는 필수입니다.")
    private String memAddress;
    
    private String businessName;
    private String businessNumber;
    
    private String bankCode;
    private String account;
    private String accountOwner;
}
