package com.metamong.mt.domain.facility.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class AdditionalInfoResponseDto {
    private Long addinfoId;
    private String addinfoDesc;
}
