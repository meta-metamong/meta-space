package com.metamong.mt.domain.member.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateRequestDto {

    @NotEmpty(message = "이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Size(min = 6, message = "비밀번호는 6자리 이상이어야 합니다.")
    private String password;
    private String confirmPassword;

    @NotEmpty(message = "전화번호는 필수입니다.")
    private String phone;

    @Past(message = "생일은 과거의 날짜만 가능합니다.")
    private LocalDate birth; 

    @NotEmpty(message = "우편번호는 필수입니다.")
    private String postalCode;

    @NotEmpty(message = "상세 주소는 필수입니다.")
    private String detailAddress;

    @NotEmpty(message = "주소는 필수입니다.")
    private String address;
    
    private String businessName;
    private String businessRegistrationNumber;
}
