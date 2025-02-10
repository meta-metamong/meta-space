package com.metamong.mt.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoginRequestDto {

	@NotBlank(message = "이메일은 필수입니다.")
    private String email;

	@NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}