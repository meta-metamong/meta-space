package com.metamong.mt.domain.member.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class EmailValidationCodeRequestDto {
    private String email;
    private String emailValidationCode;
}
