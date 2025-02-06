package com.metamong.mt.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
public class LoginRequestDto {

	@NotBlank(message = "이메일은 필수입니다.")
    private String email;

	@NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}