package com.metamong.mt.domain.member.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordConfirmRequestDto {
    
    @NotEmpty(message="비밀번호는 입력되어야 합니다.")
    private String password;
}
