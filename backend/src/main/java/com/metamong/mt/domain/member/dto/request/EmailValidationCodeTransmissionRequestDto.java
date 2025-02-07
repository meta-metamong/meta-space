package com.metamong.mt.domain.member.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class EmailValidationCodeTransmissionRequestDto {
    private String email;
}
