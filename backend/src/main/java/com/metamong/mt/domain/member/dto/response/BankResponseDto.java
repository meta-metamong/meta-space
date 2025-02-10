package com.metamong.mt.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class BankResponseDto {
    private String bankCode;
    private String bankName;
}
