package com.metamong.mt.domain.member.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class BankListResponseDto {
    private List<BankResponseDto> bankList;
}
