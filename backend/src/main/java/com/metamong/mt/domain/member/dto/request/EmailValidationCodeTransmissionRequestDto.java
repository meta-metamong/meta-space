package com.metamong.mt.domain.member.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class EmailValidationCodeTransmissionRequestDto {
    
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    private String email;
}
