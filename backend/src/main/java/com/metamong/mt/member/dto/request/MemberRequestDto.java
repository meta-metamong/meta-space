package com.metamong.mt.member.dto.request;

import java.util.Date;

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
public class MemberRequestDto {

    @NotEmpty(message = "아이디는 필수입니다.")
    private String userid;

    @NotEmpty(message = "이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Size(min = 6, message = "비밀번호는 6자리 이상이어야 합니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수입니다.")
    private String confirmPassword;

    @NotEmpty(message = "전화번호는 필수입니다.")
    private String phone;

    @NotEmpty(message = "역할은 필수입니다.")
    private String role;

    @Past(message = "생일은 과거의 날짜만 가능합니다.")
    private Date birth; // 생일 필드는 과거 날짜만 가능하도록 설정

    @NotEmpty(message = "우편번호는 필수입니다.")
    private String postal_code;

    @NotEmpty(message = "상세 주소는 필수입니다.")
    private String detail_address;

    @NotEmpty(message = "주소는 필수입니다.")
    private String address;
}
